package com.rpg.springCat.repository;

import com.rpg.springCat.model.Task;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;


@Repository
public class InMemoryTaskDAO {
    private final List<Task> TASKS = new ArrayList<>();

    public List<Task> findAllTasks() {
        return TASKS;
    }

    public Task saveTask(Task task) {
        task.setIsComplete(false);
        TASKS.add(task);
        return task;
    }

    public Task findById(String id) {
        return TASKS.stream()
                .filter(el -> el.getTaskName().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Task updateTask(Task task) {
        var taskIndex = IntStream.range(0, TASKS.size())
                .filter(i -> TASKS.get(i).getTaskId().equals(task.getTaskId()))
                .findFirst()
                .orElse(-1);

        if (taskIndex > -1) {
            TASKS.set(taskIndex, task);
            return task;
        }
        return null;
    }

    public void deleteTask(String taskId) {
        TASKS.removeIf(t -> t.getTaskId().equals(taskId));
    }
}