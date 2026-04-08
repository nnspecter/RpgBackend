package com.rpg.springCat.service;

import com.rpg.springCat.model.Character;
import com.rpg.springCat.model.Metrics;
import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.CharacterRepository;
import com.rpg.springCat.repository.MetricsRepository;
import com.rpg.springCat.repository.MyUserRepository;
import com.rpg.springCat.security.JwtService;
import com.rpg.springCat.security.RefreshToken;
import com.rpg.springCat.security.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final MyUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CharacterRepository characterRepository;
    private final MetricsRepository metricsRepository;
    private final RefreshTokenService refreshTokenService;

    /**
     * Регистрация: сохраняем юзера, создаём персонажа и метрики,
     * возвращаем сразу accessToken + refreshToken.
     */
    public Map<String, String> register(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = repository.save(user);

        // Создаём персонажа
        Character character = new Character();
        character.setName("character");
        character.setXp(0);
        character.setUser(savedUser);
        characterRepository.save(character);

        // Создаём метрики
        Metrics metrics = Metrics.builder()
                .count(0)
                .streak(0)
                .lastUpdate(LocalDate.now().minusDays(2))
                .user(savedUser)
                .build();
        metricsRepository.save(metrics);

        String accessToken = jwtService.generateAccessToken(savedUser.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        );
    }
}