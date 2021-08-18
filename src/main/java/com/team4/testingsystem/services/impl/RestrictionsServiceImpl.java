package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;

@Service
public class RestrictionsServiceImpl implements RestrictionsService {

    @Override
    public void checkOwnerIsCurrentUser(Test test) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        if (!test.getUser().getId().equals(currentUserId)) {
            throw new AccessControlException("The test has another owner");
        }
    }

    @Override
    public void checkStartedStatus(Test test) {
        if (!test.getStatus().name().equals(Status.STARTED.name())) {
            throw new AccessControlException("The test isn't started");
        }
    }

    @Override
    public void checkTestContainsQuestion(Test test, Question question) {
        if (!test.getQuestions().contains(question)) {
            throw new QuestionNotFoundException("The test doesn't contain the question with id = "
                + question.getId());
        }
    }
}
