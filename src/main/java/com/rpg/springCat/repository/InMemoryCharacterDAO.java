package com.rpg.springCat.repository;

import org.springframework.stereotype.Repository;
import com.rpg.springCat.model.Character;

@Repository
public class InMemoryCharacterDAO {
    private Character CHARACTER;

    public Character saveCharacter(Character character) {
        if (this.CHARACTER != null) {
            throw new RuntimeException("Character already exists");
        }
        this.CHARACTER = character;
        return this.CHARACTER;
    }

    public Character findCharacter(){return CHARACTER;}

    public Character updateCharacter(Character character) {
        if (this.CHARACTER == null) {
            throw new RuntimeException("Character not found");
        }
        this.CHARACTER = character;
        return this.CHARACTER;
    }
}