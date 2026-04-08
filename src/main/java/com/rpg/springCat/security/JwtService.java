package com.rpg.springCat.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // ⚠️ В проде вынести в application.properties / env переменную
    private final String SECRET = "supersecretkeysupersecretkeysupersecretkey";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Access token — короткоживущий (1 час).
     * Передаётся в заголовке Authorization: Bearer <token> при каждом запросе.
     */
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) // 1 час
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Псевдоним для обратной совместимости
    public String generateToken(String username) {
        return generateAccessToken(username);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}