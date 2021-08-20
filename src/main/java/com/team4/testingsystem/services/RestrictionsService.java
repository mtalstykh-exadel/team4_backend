package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;

public interface RestrictionsService {

    void checkOwnerIsCurrentUser(Test test, Long currentUserId);

    void checkStatus(Test test, Status status);

    void checkGradeIsCorrect(int grade);

    void checkModuleIsEssayOrSpeaking(Question question);

    void checkTestContainsQuestion(Test test, Question question);

    void checkHasAssignedCoach(Test test);

    void checkHasNoAssignedCoaches(Test test);

    void checkCoachIsCurrentUser(Test test);

    void checkHasNoAssignedTests(User user);

    void checkIsAssigned(Test test);

    void checkNotSelfAssign(User user);

    void checkNotSelfDeassign(User user);

    void checkHasNoStartedTests(Long userId);

    void checkNotSelfAssignmentCoach(Test test, Long coachId);

    void checkNotVerifiedForCoachDeassign(Test test);

    void checkNotSelfAssignAdmin(Test test);

    void checkNotSelfDeassignAdmin(Test test);

}
