package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;

public interface RestrictionsService {

    void checkOwnerIsCurrentUser(Test test);

    void checkStartedStatus(Test test);

    void checkTestContainsQuestion(Test test, Question question);
}
