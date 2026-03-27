package com.rpg.springCat.service;

import com.rpg.springCat.model.Character;
import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.CharacterRepository;
import com.rpg.springCat.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CharacterService {

    private final CharacterRepository repository;
    private final MyUserRepository userRepository;

    // 🔥 теперь ищем внутри пользователя
    public Character findCharacter(Authentication auth) {
        return repository.findFirstByUserUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Character not found"));
    }

    // 🔥 один персонаж на пользователя
    public Character saveCharacter(Character character, Authentication auth) {

        if (!repository.findByUserUsername(auth.getName()).isEmpty()) {
            throw new RuntimeException("Character already exists");
        }

        MyUser user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        character.setUser(user);

        return repository.save(character);
    }

    public Character updateCharacter(Character character, Authentication auth) {

        Character existing = findCharacter(auth);

        // сохраняем id (твоя логика)
        character.setId(existing.getId());

        // 🔥 сохраняем пользователя
        character.setUser(existing.getUser());

        return repository.save(character);
    }
}