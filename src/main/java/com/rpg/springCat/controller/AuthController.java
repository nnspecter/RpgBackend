package com.rpg.springCat.controller;

import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody MyUser user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody MyUser user) {
        return authService.login(user);
    }
}