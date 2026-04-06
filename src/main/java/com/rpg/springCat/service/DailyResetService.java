package com.rpg.springCat.service;

import com.rpg.springCat.model.Task;
import com.rpg.springCat.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DailyResetService {

    private final TaskRepository taskRepository;

    // Каждый день в 00:00 по серверному времени
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyTasks() {
        LocalDate today = LocalDate.now();

        List<Task> tasksToReset = taskRepository
                .findByLastResetDateBeforeOrLastResetDateIsNull(today);

        tasksToReset.forEach(task -> {
            task.setIsComplete(false);
            task.setLastResetDate(today);
        });

        taskRepository.saveAll(tasksToReset);
        log.info("Daily reset: сброшено {} задач", tasksToReset.size());
    }
}