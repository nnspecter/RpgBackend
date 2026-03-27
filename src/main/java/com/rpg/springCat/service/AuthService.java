package com.rpg.springCat.service;

import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.MyUserRepository;
import com.rpg.springCat.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final MyUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return jwtService.generateToken(user.getUsername());
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