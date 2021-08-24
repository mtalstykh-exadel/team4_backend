package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.Timer;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.TimerRepository;
import com.team4.testingsystem.services.TimerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.TimerTask;

@Service
@AllArgsConstructor
public class TimerServiceImpl implements TimerService {
    private final TimerRepository timerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createTimer(Test test, Status status, Instant scheduledTime) {
        Timer timer = new Timer(test, status, scheduledTime);
        timerRepository.save(timer);
        startTimer(timer);
    }

    @Override
    public boolean existsById(Long timerId) {
        return timerRepository.existsById(timerId);
    }

    @Override
    public void deleteTimer(Long testId, Status status) {
        timerRepository.deleteByTestIdAndStatus(testId, status);
    }

    private void startTimer(Timer databaseTimer) {
        Test test = databaseTimer.getTest();
        long delay = databaseTimer.getScheduledTime().toEpochMilli() - Instant.now().toEpochMilli();

        TimerTask timerTask = new TimerTask() {
            public void run() {
                eventPublisher.publishEvent(databaseTimer);
                deleteTimer(test.getId(), databaseTimer.getStatus());
            }
        };

        java.util.Timer timer = new java.util.Timer();
        if (delay <= 0) {
            timerTask.run();
            timer.cancel();
        } else {
            timer.schedule(timerTask, delay);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startAllTimers() {
        timerRepository.findAll().forEach(this::startTimer);
    }
}
