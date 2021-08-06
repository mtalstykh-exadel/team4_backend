package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface TestsService {

    Iterable<Test> getAll();

    Test getById(long id);

    List<Test> getByUserId(long userId);

    List<Test> getByStatuses(Status[] status);

    Test save(Test test);

    long startForUser(long userId, Levels level);

    long assignForUser(long userId, Levels level, LocalDateTime deadline);

    TestDTO start(long id);

    void finish(long id);

    void updateEvaluation(long id, int newEvaluation);

    void removeById(long id);

    void assignCoach(long id, long coachId);

    void deassignCoach(long id);
}
