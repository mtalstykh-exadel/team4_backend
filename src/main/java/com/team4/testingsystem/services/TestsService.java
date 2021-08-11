package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface TestsService {

    Test getById(long id);

    List<Test> getByUserId(long userId);

    List<Test> getByStatuses(Status[] status);

    List<UserTest> getAllUsersAndAssignedTests();

    Test save(Test test);

    long startForUser(long userId, Levels level);

    long assignForUser(long userId, Levels level, LocalDateTime deadline, Priority priority);

    Test start(long id);

    void deassign(long id);

    void finish(long id);

    void update(long id);

    void assignCoach(long id, long coachId);

    void deassignCoach(long id);
}
