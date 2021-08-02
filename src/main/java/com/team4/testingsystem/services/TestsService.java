package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;

import java.util.List;

public interface TestsService {

    Iterable<Test> getAll();

    Test getById(long id);

    Iterable<Test> getByUserId(long userId);

    List<Test> getByStatus(Status status);

    Test save(Test test);

    long createForUser(long userId, Levels level);

    void start(long id);

    void finish(long id, int evaluation);

    void updateEvaluation(long id, int newEvaluation);

    void removeById(long id);

    void assignCoach(long id, long coachId);

    void deassignCoach(long id);
}
