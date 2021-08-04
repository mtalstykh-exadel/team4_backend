package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final ChosenOptionService chosenOptionService;
    private final TestsService testsService;

    @Autowired
    public TestEvaluationServiceImpl(ChosenOptionService chosenOptionService, TestsService testsService) {
        this.chosenOptionService = chosenOptionService;
        this.testsService = testsService;
    }

    public int getEvaluationByTestId(long testId) {
        TestQuestionID testQuestionID = new TestQuestionID();
        Test test = testsService.getById(testId);
        List<Question> questions = test.getQuestions();
        testQuestionID.setTest(test);

        return calcEvaluation(testQuestionID, questions);

    }

    private int calcEvaluation(TestQuestionID testQuestionID, List<Question> questions) {
        int evaluation = 0;
        List<Answer> answers;
        Answer chosenAnswer = chosenOptionService.getById(testQuestionID).getAnswer();

        for (Question question : questions) {
            answers = question.getAnswers();
            testQuestionID.setQuestion(question);

            if (isCorrect(chosenAnswer, answers)) evaluation++;

        }

        return evaluation;
    }

    private boolean isCorrect(Answer chosenAnswer, List<Answer> answers) {
        return answers.stream()
                .filter(answer -> (answer.getId() == chosenAnswer.getId()))
                .findFirst()
                .get()
                .isCorrect();
    }
}
