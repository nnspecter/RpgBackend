package com.rpg.springCat.controller;

import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.security.JwtService;
import com.rpg.springCat.security.RefreshToken;
import com.rpg.springCat.security.RefreshTokenService;
import com.rpg.springCat.service.AuthService;
import com.rpg.springCat.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final MyUserService userService;
    private final AuthService authService;

    /**
     * POST /auth/register
     * Body: { "username": "...", "password": "..." }
     * Создаёт юзера, персонажа, метрики → возвращает токены.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MyUser user) {
        return ResponseEntity.ok(authService.register(user));
    }

    /**
     * POST /auth/login
     * Body: { "username": "...", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        MyUser user = userService.findByUsername(username);

        String accessToken = jwtService.generateAccessToken(username);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        ));
    }

    /**
     * POST /auth/refresh
     * Body: { "refreshToken": "..." }
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String tokenValue = body.get("refreshToken");

        RefreshToken refreshToken = refreshTokenService.verifyAndExtend(tokenValue);
        String username = refreshToken.getUser().getUsername();
        String newAccessToken = jwtService.generateAccessToken(username);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", refreshToken.getToken()
        ));
    }

    /**
     * POST /auth/logout
     * Body: { "refreshToken": "..." }
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        refreshTokenService.deleteByToken(body.get("refreshToken"));
        return ResponseEntity.ok(Map.of("message", "Вы вышли из системы"));
    }

    /**
     * POST /auth/logout-all
     */
    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        MyUser user = userService.findByUsername(username);
        refreshTokenService.deleteAllByUser(user);
        return ResponseEntity.ok(Map.of("message", "Выход со всех устройств выполнен"));
    }
}