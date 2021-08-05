package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.TestEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final ChosenOptionService chosenOptionService;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService) {
        this.chosenOptionService = chosenOptionService;
    }

    public int getEvaluationByTest(Test test) {
        Answer chosenAnswer;
        TestQuestionID testQuestionID = new TestQuestionID();
        List<Question> questions = test.getQuestions();
        testQuestionID.setTest(test);
        int evaluation = 0;

        for (Question question : questions) {
            testQuestionID.setQuestion(question);
            chosenAnswer = chosenOptionService.getById(testQuestionID).getAnswer();

            if (chosenAnswer.isCorrect()) {
                evaluation++;
            }

        }
        return evaluation;
    }
}
