package com.rpg.springCat.repository;

import com.rpg.springCat.model.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetricsRepository extends JpaRepository<Metrics, Long> {

    List<Metrics> findByUserUsername(String username);

    Optional<Metrics> findFirstByUserUsername(String username);
}