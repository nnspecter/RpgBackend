package com.rpg.springCat.repository;

import com.rpg.springCat.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserUsername(String username);

    Optional<Task> findByTaskIdAndUserUsername(Long taskId, String username);

    // 🆕 найти все задачи, где дата сброса не сегодня (или null)
    List<Task> findByLastResetDateBeforeOrLastResetDateIsNull(LocalDate date);
}