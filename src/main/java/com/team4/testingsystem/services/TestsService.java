package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface TestsService {

    Test getById(long id);

    Test getByIdWithRestrictions(long id);

    List<Test> getByUserId(long userId, Pageable pageable);

    List<Test> getByStatuses(Status[] status, Pageable pageable);

    List<Test> getAllUnverifiedTests(Pageable pageable);

    List<Test> getAllUnverifiedTestsByCoach(long coachId, Pageable pageable);

    List<UserTest> getAllUsersAndAssignedTests(Pageable pageable);

    List<Test> getTestsByUserIdAndLevel(long userId, Levels level, Pageable pageable);

    Test startTestVerification(long testId);

    Test save(Test test);

    long createNotAssigned(long userId, Levels level);

    long createAssigned(long userId, Levels level, Instant deadline, Priority priority);

    Test startNotAssigned(long testId);

    Test startAssigned(long testId);

    void deassign(long id);

    void selfFinish(long id);

    void coachSubmit(long id);

    void assignCoach(long id, long coachId);

    void deassignCoach(long id);
}
