package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.IllegalGradeException;
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
    public void checkStatus(Test test, Status status) {
        if (!test.getStatus().name().equals(status.name())) {
            throw new AccessControlException("The test isn't " + status.name().toLowerCase());

        }
    }

    @Override
    public void checkTestContainsQuestion(Test test, Question question) {
        if (!test.getQuestions().contains(question)) {
            throw new QuestionNotFoundException("The test doesn't contain the question with id = "
                + question.getId());
        }
    }


    @Override
    public void checkGradeIsCorrect(int grade){
        if (grade < 0 || grade > 10){
            throw new IllegalGradeException();
        }
    }

    @Override
    public void checkModule(Question question){
        if (!question.getModule().getName().equals(Modules.ESSAY.getName())
        && !question.getModule().getName().equals(Modules.SPEAKING.getName())){
            throw new AccessControlException("Coach can grade only essay and speaking");
        }
    }

    @Override
    public void checkCoachIsCurrentUser(Test test){
            Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

            if (!test.getUser().getId().equals(currentUserId)) {
                throw new AccessControlException("The test has another coach");
            }
        }
}
