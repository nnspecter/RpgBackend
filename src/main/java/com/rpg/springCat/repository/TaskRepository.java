package com.rpg.springCat.repository;

import com.rpg.springCat.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserUsername(String username);

    Optional<Task> findByTaskIdAndUserUsername(Long taskId, String username);

}