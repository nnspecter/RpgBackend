package com.rpg.springCat.service;

import com.rpg.springCat.model.Metrics;
import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.MetricsRepository;
import com.rpg.springCat.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class MetricsService {

    private final MetricsRepository repository;
    private final MyUserRepository userRepository;

    // 🔥 теперь с JWT
    public Metrics findMetrics(Authentication auth) {

        return repository.findFirstByUserUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Metrics not found"));
    }

    public Metrics saveMetrics(Metrics metrics, Authentication auth) {

        if (!repository.findByUserUsername(auth.getName()).isEmpty()) {
            throw new RuntimeException("Metrics already exists");
        }

        MyUser user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        metrics.setUser(user);

        return repository.save(metrics);
    }

    public Metrics updateMetrics(Metrics metrics, Authentication auth) {

        Metrics existing = findMetrics(auth);

        if (existing == null) {
            throw new RuntimeException("Metrics not found");
        }

        // сохраняем id (твоя логика)
        metrics.setId(existing.getId());

        // 🔥 сохраняем пользователя (ВАЖНО)
        metrics.setUser(existing.getUser());

        return repository.save(metrics);
    }
}