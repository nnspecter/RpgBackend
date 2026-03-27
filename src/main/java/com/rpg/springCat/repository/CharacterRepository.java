package com.rpg.springCat.repository;

import com.rpg.springCat.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findByUserUsername(String username);

    Optional<Character> findFirstByUserUsername(String username);
}