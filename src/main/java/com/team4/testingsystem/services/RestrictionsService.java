package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Status;

public interface RestrictionsService {

    void checkOwnerIsCurrentUser(Test test);

    void checkStatus(Test test, Status status);

    void checkGradeIsCorrect(int grade);

    void checkModule(Question question);

    void checkTestContainsQuestion(Test test, Question question);

    void checkCoachIsCurrentUser(Test test);
}
