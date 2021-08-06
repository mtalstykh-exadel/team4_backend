package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
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
        return (int) test.getQuestions()
                .stream()
                .map((Question question) -> chosenOptionService.getById(new TestQuestionID(test, question)))
                .filter(chosenOption -> chosenOption.getAnswer().isCorrect())
                .count();
    }
}
