package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.services.ChosenOptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TestEvaluationServiceImplTest {
    @Mock
    private ChosenOptionService chosenOptionService;

    @InjectMocks
    private TestEvaluationServiceImpl testEvaluationService;

    private com.team4.testingsystem.entities.Test test;

    @BeforeEach
    void init() {
        List<Question> questions = new ArrayList<>();

        questions.add(Question.builder().id(1L).build());
        questions.add(Question.builder().id(2L).build());

        test = new com.team4.testingsystem.entities.Test();
        test.setQuestions(questions);

        TestQuestionID testQuestionID = new TestQuestionID();
        testQuestionID.setTest(test);
        testQuestionID.setQuestion(questions.get(0));

        ChosenOption chosenOption = new ChosenOption();
        chosenOption.setAnswer(Answer.builder().id(2L).question(questions.get(0)).isCorrect(false).build());
        chosenOptionService.save(chosenOption);

        Mockito.when(chosenOptionService.getById(testQuestionID)).thenReturn(chosenOption);

        testQuestionID = new TestQuestionID();
        testQuestionID.setTest(test);
        testQuestionID.setQuestion(questions.get(1));

        chosenOption = new ChosenOption();
        chosenOption.setAnswer(Answer.builder().id(2L).question(questions.get(1)).isCorrect(true).build());
        chosenOptionService.save(chosenOption);

        Mockito.when(chosenOptionService.getById(testQuestionID)).thenReturn(chosenOption);
    }

    @Test
    void getEvaluationByTestThatHasOneCorrectSelectedAnswer() {
        Assertions.assertEquals(1, testEvaluationService.getEvaluationByTest(test));
    }
}