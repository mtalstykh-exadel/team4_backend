package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.ChosenOptionDTO;
import com.team4.testingsystem.model.entity.Answer;
import com.team4.testingsystem.model.entity.ChosenOption;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class ChosenOptionConverterTest {

    private static final Long TEST_ID = 1L;
    private static final Long QUESTION_ID = 2L;
    private static final Long ANSWER_ID = 3L;

    @Mock
    private TestsService testsService;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private ChosenOptionConverter converter;

    @Mock
    private com.team4.testingsystem.model.entity.Test test;

    @Mock
    private Question question;

    @Mock
    private Answer answer;

    private ChosenOptionDTO dto;

    @BeforeEach
    void init() {
        dto = new ChosenOptionDTO(TEST_ID, QUESTION_ID, ANSWER_ID);
    }

    @Test
    void convertToEntityTestNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> converter.convertToEntity(dto));
    }

    @Test
    void convertToEntityQuestionNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class, () -> converter.convertToEntity(dto));
    }

    @Test
    void convertToEntityAnswerNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenReturn(question);
        Mockito.when(answerService.getById(ANSWER_ID)).thenThrow(AnswerNotFoundException.class);

        Assertions.assertThrows(AnswerNotFoundException.class, () -> converter.convertToEntity(dto));
    }

    @Test
    void convertToEntityAnswerNotFromQuestion() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenReturn(question);
        Mockito.when(answerService.getById(ANSWER_ID)).thenReturn(answer);

        Mockito.when(answer.getQuestion()).thenReturn(EntityCreatorUtil.createQuestion());

        Assertions.assertThrows(AnswerNotFoundException.class, () -> converter.convertToEntity(dto));
    }

    @Test
    void convertToEntitySuccess() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenReturn(question);
        Mockito.when(answerService.getById(ANSWER_ID)).thenReturn(answer);

        Mockito.when(answer.getQuestion()).thenReturn(question);

        ChosenOption entity = converter.convertToEntity(dto);

        Assertions.assertEquals(test, entity.getId().getTest());
        Assertions.assertEquals(question, entity.getId().getQuestion());
        Assertions.assertEquals(answer, entity.getAnswer());
    }
}
