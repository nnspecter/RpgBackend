package com.rpg.springCat.service;

import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserService implements UserDetailsService {

    private final MyUserRepository repository;

    /**
     * Используется Spring Security для аутентификации.
     * Возвращает UserDetails (не MyUser), поэтому роли и пароль берём отсюда.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Возвращает саму сущность MyUser — нужен для создания RefreshToken,
     * logout-all и других мест где нужна JPA-сущность, а не UserDetails.
     */
    public MyUser findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}