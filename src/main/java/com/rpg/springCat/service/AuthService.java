package com.rpg.springCat.service;

import com.rpg.springCat.model.Character;
import com.rpg.springCat.model.Metrics;
import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.CharacterRepository;
import com.rpg.springCat.repository.MetricsRepository;
import com.rpg.springCat.repository.MyUserRepository;
import com.rpg.springCat.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AuthService {

    private final MyUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CharacterRepository characterRepository;
    private final MetricsRepository metricsRepository;

    public String register(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = repository.save(user);

        // создаём персонажа
        Character character = new Character();
        character.setName("character");
        character.setXp(0);
        character.setUser(savedUser);
        characterRepository.save(character);

        // создаём метрики
        Metrics metrics = Metrics.builder()
                .count(0)
                .streak(0)
                .lastUpdate(LocalDate.now().minusDays(2))
                .user(savedUser)
                .build();
        metricsRepository.save(metrics);

        return jwtService.generateToken(savedUser.getUsername());
    }

    public String login(MyUser user) {
        var dbUser = repository.findByUsername(user.getUsername())
                .orElseThrow();

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(dbUser.getUsername());
    }
}