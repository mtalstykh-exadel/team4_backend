package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.CoachGradeAlreadyExistsException;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.CoachGradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CoachGradeControllerTest {
    @Mock
    private CoachGradeService gradeService;

    @InjectMocks
    private CoachGradeController gradeController;

    @Mock
    private CoachGrade coachGrade;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private Question question;

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
        Mockito.when(gradeService.getGrade(testId, questionId)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.getGrade(testId, questionId));
    }

    @Test
    void getGradeQuestionNotFound() {
        Mockito.when(gradeService.getGrade(testId, questionId)).thenThrow(QuestionNotFoundException.class);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeController.getGrade(testId, questionId));
    }

    @Test
    void getGradeNotExists() {
        Mockito.when(gradeService.getGrade(testId, questionId)).thenThrow(GradeNotFoundException.class);
        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeController.getGrade(testId, questionId));
    }

    @Test
    void getGradeSuccess() {
        Mockito.when(test.getId()).thenReturn(testId);
        Mockito.when(question.getId()).thenReturn(questionId);

        Mockito.when(coachGrade.getTest()).thenReturn(test);
        Mockito.when(coachGrade.getQuestion()).thenReturn(question);
        Mockito.when(coachGrade.getGrade()).thenReturn(grade);

        Mockito.when(gradeService.getGrade(testId, questionId)).thenReturn(coachGrade);

        CoachGradeDTO gradeDTO = gradeController.getGrade(testId, questionId);
        Assertions.assertEquals(testId, gradeDTO.getTestId());
        Assertions.assertEquals(questionId, gradeDTO.getQuestionId());
        Assertions.assertEquals(grade, gradeDTO.getGrade());
    }

    @Test
    void getGradesTestNotFound() {
        Mockito.when(gradeService.getGradesByTest(testId)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.getGrades(testId));
    }

    @Test
    void getGradesSuccess() {
        Mockito.when(test.getId()).thenReturn(testId);
        Mockito.when(question.getId()).thenReturn(questionId);

        Mockito.when(coachGrade.getTest()).thenReturn(test);
        Mockito.when(coachGrade.getQuestion()).thenReturn(question);
        Mockito.when(coachGrade.getGrade()).thenReturn(grade);

        Mockito.when(gradeService.getGradesByTest(testId)).thenReturn(Collections.singletonList(coachGrade));

        List<CoachGradeDTO> grades = gradeController.getGrades(testId);
        Assertions.assertEquals(1, grades.size());
        Assertions.assertEquals(testId, grades.get(0).getTestId());
        Assertions.assertEquals(questionId, grades.get(0).getQuestionId());
        Assertions.assertEquals(grade, grades.get(0).getGrade());
    }

    @Test
    void createGradeTestNotFound() {
        Mockito.doThrow(TestNotFoundException.class).when(gradeService).createGrade(gradeRequest);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.createGrade(gradeRequest));
    }

    @Test
    void createGradeQuestionNotFound() {
        Mockito.doThrow(QuestionNotFoundException.class).when(gradeService).createGrade(gradeRequest);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeController.createGrade(gradeRequest));
    }

    @Test
    void createGradeAlreadyExists() {
        Mockito.doThrow(CoachGradeAlreadyExistsException.class).when(gradeService).createGrade(gradeRequest);

        Assertions.assertThrows(CoachGradeAlreadyExistsException.class,
                () -> gradeController.createGrade(gradeRequest));
    }

    @Test
    void createGradeQuestionSuccess() {
        CoachGradeDTO gradeDTO = CoachGradeDTO.builder().build();
        gradeController.createGrade(gradeDTO);

        ArgumentCaptor<CoachGradeDTO> captor = ArgumentCaptor.forClass(CoachGradeDTO.class);
        Mockito.verify(gradeService).createGrade(captor.capture());

        Assertions.assertEquals(gradeDTO, captor.getValue());
    }

    @Test
    void updateGradeNotExists() {
        Mockito.doThrow(GradeNotFoundException.class).when(gradeService).updateGrade(gradeRequest);
        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeController.updateGrade(gradeRequest));
    }

    @Test
    void updateGradeSuccess() {
        CoachGradeDTO gradeDTO = CoachGradeDTO.builder().build();
        gradeController.updateGrade(gradeDTO);

        ArgumentCaptor<CoachGradeDTO> captor = ArgumentCaptor.forClass(CoachGradeDTO.class);
        Mockito.verify(gradeService).updateGrade(captor.capture());

        Assertions.assertEquals(gradeDTO, captor.getValue());
    }
}
