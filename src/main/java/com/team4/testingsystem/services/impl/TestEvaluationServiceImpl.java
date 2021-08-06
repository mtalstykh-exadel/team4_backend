package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final ChosenOptionService chosenOptionService;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService) {
        this.chosenOptionService = chosenOptionService;
    }

    public int getEvaluationByTest(Test test) {
        return (int) chosenOptionService.getChosenOptionByTest(test)
                .stream()
                .map(ChosenOption::getAnswer)
                .filter(Answer::isCorrect)
                .count();
    }
}
