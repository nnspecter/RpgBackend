package com.rpg.springCat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    private String taskName;
    private String description;
    private Integer time;
    private Boolean isComplete;

    private LocalDate lastResetDate; // 🆕 дата последнего сброса

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private MyUser user;
}