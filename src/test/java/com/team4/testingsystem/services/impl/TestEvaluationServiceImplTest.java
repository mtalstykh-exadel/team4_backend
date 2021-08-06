package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.ChosenOptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TestEvaluationServiceImplTest {
    @Mock
    private ChosenOptionService chosenOptionService;

    @InjectMocks
    private TestEvaluationServiceImpl testEvaluationService;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @BeforeEach
    void init() {
        Question question = Question.builder().id(1L).build();
        ChosenOption chosenOption = new ChosenOption();
        chosenOption.setAnswer(Answer.builder().id(2L).question(question).isCorrect(false).build());
        chosenOptionService.save(chosenOption);

        Mockito.when(chosenOptionService.getChosenOptionByTest(test)).thenReturn(List.of(chosenOption));

        question = Question.builder().id(2L).build();
        chosenOption = new ChosenOption();
        chosenOption.setAnswer(Answer.builder().id(2L).question(question).isCorrect(true).build());
        chosenOptionService.save(chosenOption);

        Mockito.when(chosenOptionService.getChosenOptionByTest(test)).thenReturn(List.of(chosenOption));

    }

    @Test
    void getEvaluationByTestThatHasOneCorrectSelectedAnswer() {
        Assertions.assertEquals(1, testEvaluationService.getEvaluationByTest(test));
    }
}