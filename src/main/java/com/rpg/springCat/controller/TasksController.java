package com.rpg.springCat.controller;

import com.rpg.springCat.model.Task;
import com.rpg.springCat.service.TasksService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TasksController {

    private final TasksService service;

    @GetMapping
    public List<Task> findAllTasks(Authentication auth) {
        return service.findAllTasks(auth);
    }

    @PostMapping("/new")
    public Task saveTask(@RequestBody Task task, Authentication auth) {
        return service.saveTask(task, auth);
    }

    @GetMapping("/{taskId}")
    public Task findById(@PathVariable Long taskId, Authentication auth) {
        return service.findById(taskId, auth);
    }

    @PutMapping("/upd")
    public Task updateTask(@RequestBody Task task, Authentication auth) {
        return service.updateTask(task, auth);
    }

    @DeleteMapping("/del/{taskId}")
    public void deleteTask(@PathVariable Long taskId, Authentication auth) {
        service.deleteTask(taskId, auth);
    }
}