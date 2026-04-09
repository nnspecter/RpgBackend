package com.rpg.springCat.security;

import com.rpg.springCat.model.MyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    // 2 месяца = 60 дней
    private static final long REFRESH_TOKEN_DAYS = 60;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Создаёт новый refresh token для пользователя.
     * Старые токены НЕ удаляем — можно логиниться с нескольких устройств.
     * Если нужна только одна сессия — раскомментируй deleteAllByUser.
     */
    @Transactional
    public RefreshToken createRefreshToken(MyUser user) {
        // refreshTokenRepository.deleteAllByUser(user); // раскомментировать для single-session

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepository.save(token);
    }

    /**
     * Находит токен в БД, проверяет истечение.
     * Если токен валиден — продлевает срок на 2 месяца (sliding expiration).
     */
    @Transactional
    public RefreshToken verifyAndExtend(String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token не найден"));

        if (refreshToken.isExpired()) {
            // Токен протух — удаляем и просим логиниться заново
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token истёк. Войдите снова.");
        }

        // Продлеваем на 2 месяца с момента текущего использования
        refreshToken.setExpiresAt(Instant.now().plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS));
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Удалить конкретный токен (logout с одного устройства).
     */
    @Transactional
    public void deleteByToken(String tokenValue) {
        refreshTokenRepository.findByToken(tokenValue)
                .ifPresent(refreshTokenRepository::delete);
    }

    /**
     * Удалить все токены пользователя (logout со всех устройств).
     */
    @Transactional
    public void deleteAllByUser(MyUser user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}