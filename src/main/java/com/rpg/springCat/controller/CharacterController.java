package com.rpg.springCat.controller;

import com.rpg.springCat.model.Character;
import com.rpg.springCat.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/character")
@AllArgsConstructor
public class CharacterController {

    private final CharacterService service;

    @GetMapping
    public Character findCharacter(Authentication auth){
        return service.findCharacter(auth);
    }

    @PostMapping("/new")
    public Character saveCharacter(@RequestBody Character character, Authentication auth){
        return service.saveCharacter(character, auth);
    }

    @PutMapping("/upd")
    public Character updateCharacter(@RequestBody Character character, Authentication auth){
        return service.updateCharacter(character, auth);
    }
}