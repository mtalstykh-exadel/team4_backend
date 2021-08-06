package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final ChosenOptionService chosenOptionService;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService) {
        this.chosenOptionService = chosenOptionService;
    }

    public int getEvaluationByTest(Test test) {
        List<ChosenOption> chosenOptions = new ArrayList<>();
        chosenOptionService.getChosenOptionByTest(test).iterator().forEachRemaining(chosenOptions::add);
        return (int) chosenOptions.stream().map(ChosenOption::getAnswer).filter(Answer::isCorrect).count();
    }
}
