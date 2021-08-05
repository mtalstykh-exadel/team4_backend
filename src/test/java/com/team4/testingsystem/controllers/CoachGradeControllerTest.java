package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.CoachGradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private TestQuestionID testQuestionID;

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
    void getGradesTestNotFound() {
        Mockito.when(gradeService.getGradesByTest(testId)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.getGrades(testId));
    }

    @Test
    void getGradesSuccess() {
        Mockito.when(test.getId()).thenReturn(testId);
        Mockito.when(question.getId()).thenReturn(questionId);

        Mockito.when(coachGrade.getId()).thenReturn(testQuestionID);

        Mockito.when(testQuestionID.getTest()).thenReturn(test);
        Mockito.when(testQuestionID.getQuestion()).thenReturn(question);
        Mockito.when(coachGrade.getGrade()).thenReturn(grade);

        Mockito.when(gradeService.getGradesByTest(testId)).thenReturn(Collections.singletonList(coachGrade));

        List<CoachGradeDTO> grades = gradeController.getGrades(testId);
        Assertions.assertEquals(1, grades.size());
        Assertions.assertEquals(testId, grades.get(0).getTestId());
        Assertions.assertEquals(questionId, grades.get(0).getQuestionId());
        Assertions.assertEquals(grade, grades.get(0).getGrade());
    }
    @Test
    void createGradeQuestionSuccess() {
        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(testId)
                .questionId(questionId)
                .grade(grade)
                .build();
        gradeController.add(gradeDTO);

        Mockito.verify(gradeService).add(testId, questionId, grade);
    }

    @Test
    void createGradeTestNotFound() {
        Mockito.doThrow(TestNotFoundException.class)
                .when(gradeService).add(testId, questionId, grade);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.add(gradeRequest));
    }

    @Test
    void createGradeQuestionNotFound() {
        Mockito.doThrow(QuestionNotFoundException.class)
                .when(gradeService).add(testId, questionId, grade);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeController.add(gradeRequest));
    }



}
