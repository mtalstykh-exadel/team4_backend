package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.enums.Status;

import java.time.Instant;

public interface TimerService {
    void createTimer(Test test, Status status, Instant scheduledTime);

    boolean existsById(Long timerId);

    void deleteTimer(Long testId, Status status);
}
