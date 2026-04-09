package com.rpg.springCat.service;

import com.rpg.springCat.model.MyUser;
import com.rpg.springCat.model.Task;
import com.rpg.springCat.repository.TaskRepository;
import com.rpg.springCat.repository.CharacterRepository;
import com.rpg.springCat.repository.MetricsRepository;
import com.rpg.springCat.repository.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TasksService {

    private final TaskRepository taskRepository;
    private final CharacterRepository characterRepository;
    private final MetricsRepository metricsRepository;
    private final MyUserRepository userRepository;

    public List<Task> findAllTasks(Authentication auth) {
        List<Task> tasks = taskRepository.findByUserUsername(auth.getName());
        LocalDate today = LocalDate.now();

        List<Task> tasksToReset = tasks.stream()
                .filter(task -> Boolean.TRUE.equals(task.getIsComplete())
                        && !today.equals(task.getLastResetDate()))
                .toList();

        if (!tasksToReset.isEmpty()) {
            tasksToReset.forEach(task -> {
                task.setIsComplete(false);
                task.setLastResetDate(today);
            });
            taskRepository.saveAll(tasksToReset);
        }

        return tasks;
    }


    public Task saveTask(Task task, Authentication auth) {
        MyUser user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        task.setUser(user);
        task.setIsComplete(false);

        return taskRepository.save(task);
    }


    public Task findById(Long taskId, Authentication auth) {
        return taskRepository.findByTaskIdAndUserUsername(taskId, auth.getName())
                .orElse(null);
    }

    public Task updateTask(Task task, Authentication auth) {
        Task existingTask = taskRepository
                .findByTaskIdAndUserUsername(task.getTaskId(), auth.getName())
                .orElse(null);

        if (existingTask == null) {
            return null;
        }

        if (task.getTaskName() != null) {
            existingTask.setTaskName(task.getTaskName());
        }
        if (task.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.getTime() != null) {
            existingTask.setTime(task.getTime());
        }

        // isComplete обрабатываем отдельно (может быть намеренно передан)
        if (task.getIsComplete() != null) {
            boolean wasIncomplete = Boolean.FALSE.equals(existingTask.getIsComplete());
            boolean nowComplete = Boolean.TRUE.equals(task.getIsComplete());

            if (wasIncomplete && nowComplete) {

                // 🎮 XP персонажу
                var character = characterRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (character != null) {
                    character.setXp(character.getXp() + 25);
                    characterRepository.save(character);
                }

                // 📊 Метрики
                var metrics = metricsRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (metrics != null) {
                    metrics.setCount(metrics.getCount() + 1);

                    if (metrics.getLastUpdate().equals(LocalDate.now().minusDays(1))) {
                        metrics.setStreak(metrics.getStreak() + 1);
                    } else if (!Objects.equals(metrics.getLastUpdate(), LocalDate.now())) {
                        metrics.setStreak(1);
                    }

                    metrics.setLastUpdate(LocalDate.now());
                    metricsRepository.save(metrics);
                }
            }

            existingTask.setIsComplete(task.getIsComplete());
        }

        // 🔥 владелец уже в existingTask, просто сохраняем его
        return taskRepository.save(existingTask);
    }

    // 🔥 удаление только своей задачи
    public void deleteTask(Long taskId, Authentication auth) {
        Task task = taskRepository
                .findByTaskIdAndUserUsername(taskId, auth.getName())
                .orElseThrow(() -> new RuntimeException("Not your task"));

        taskRepository.delete(task);
    }
}