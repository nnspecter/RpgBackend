package com.rpg.springCat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "metrics")
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer streak;
    private Integer count;
    private LocalDate lastUpdate;

    // 🔥 привязка к пользователю
    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;
}