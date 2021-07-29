package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.CoachGradeAlreadyExistsException;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CoachGradeServiceImplTest {
    @Mock
    private CoachGradeRepository gradeRepository;

    @Mock
    private TestsService testsService;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private CoachGradeServiceImpl gradeService;

    @Mock
    private CoachGrade coachGrade;

    @Mock
    private Question question;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    private final Long testId = 1L;
    private final Long questionId = 1L;
    private final Integer grade = 10;
    private CoachGradeDTO gradeRequest;

    @BeforeEach
    void init() {
        gradeRequest = CoachGradeDTO.builder()
                .testId(testId)
                .questionId(questionId)
                .grade(grade)
                .build();
    }

    @Test
    void getGradeTestNotFound() {
        Mockito.when(testsService.getById(1L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeService.getGrade(1L, 1L));
    }

    @Test
    void getGradeQuestionNotFound() {
        Mockito.when(testsService.getById(1L)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(1L)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeService.getGrade(1L, 1L));
    }

    @Test
    void getGradeNotExists() {
        Mockito.when(testsService.getById(1L)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(1L)).thenReturn(question);
        Mockito.when(gradeRepository.findByTestAndQuestion(test, question)).thenReturn(Optional.empty());

        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.getGrade(1L, 1L));
    }

    @Test
    void getGradeSuccess() {
        Mockito.when(testsService.getById(1L)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(1L)).thenReturn(question);
        Mockito.when(gradeRepository.findByTestAndQuestion(test, question)).thenReturn(Optional.of(coachGrade));

        Assertions.assertEquals(coachGrade, gradeService.getGrade(1L, 1L));
    }

    @Test
    void getGradesByTestNotFound() {
        Mockito.when(testsService.getById(1L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeService.getGradesByTest(1L));
    }

    @Test
    void getGradesByTestSuccess() {
        Mockito.when(testsService.getById(1L)).thenReturn(test);
        ArrayList<CoachGrade> grades = new ArrayList<>();
        Mockito.when(gradeRepository.findAllByTest(test)).thenReturn(grades);

        Assertions.assertEquals(grades, gradeService.getGradesByTest(1L));
    }

    @Test
    void createGradeTestNotFound() {
        Mockito.when(testsService.getById(testId)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeService.createGrade(gradeRequest));
    }

    @Test
    void createGradeQuestionNotFound() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(questionId)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeService.createGrade(gradeRequest));
    }

    @Test
    void createGradeAlreadyExists() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(questionId)).thenReturn(question);
        Mockito.when(gradeRepository.findByTestAndQuestion(test, question)).thenReturn(Optional.of(coachGrade));

        Assertions.assertThrows(CoachGradeAlreadyExistsException.class, () -> gradeService.createGrade(gradeRequest));
    }

    @Test
    void createGradeSuccess() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(questionId)).thenReturn(question);
        Mockito.when(gradeRepository.findByTestAndQuestion(test, question)).thenReturn(Optional.empty());

        gradeService.createGrade(gradeRequest);

        ArgumentCaptor<CoachGrade> captor = ArgumentCaptor.forClass(CoachGrade.class);
        Mockito.verify(gradeRepository).save(captor.capture());

        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertEquals(question, captor.getValue().getQuestion());
        Assertions.assertEquals(grade, captor.getValue().getGrade());
    }

    @Test
    void updateGradeTestNotFound() {
        Mockito.when(testsService.getById(testId)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeService.updateGrade(gradeRequest));
    }

    @Test
    void updateGradeQuestionNotFound() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(questionId)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeService.updateGrade(gradeRequest));
    }

    @Test
    void updateGradeNotExists() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(testId)).thenReturn(question);
        Mockito.when(gradeRepository.updateGrade(test, question, grade)).thenReturn(0);

        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.updateGrade(gradeRequest));
    }

    @Test
    void updateGradeSuccess() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getQuestionById(testId)).thenReturn(question);
        Mockito.when(gradeRepository.updateGrade(test, question, grade)).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> gradeService.updateGrade(gradeRequest));
    }
}
