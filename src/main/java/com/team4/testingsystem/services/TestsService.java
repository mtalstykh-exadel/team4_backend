package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;

public interface TestsService {

    Iterable<Test> getAll();

    Test getById(long id);

    Iterable<Test> getByUserId(long userId);

    Test save(Test test);

    long createForUser(long userId, Levels level);

    void start(long id);

    void finish(long id);

    void updateEvaluation(long id, int newEvaluation);

    void removeById(long id);

    void assignCoach(long id, long coachId);

    void deassignCoach(long id);
}
