package com.rpg.springCat.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "chrctr")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer xp;

    // 🔥 привязка к пользователю
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;
}