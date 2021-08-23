package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.TestsService;
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

    @Mock
    private TestsService testsService;

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

    private static final Long GOOD_TEST_ID = 1L;
    private static final Long BAD_TEST_ID = 42L;

    private static final Long questionId = 1L;
    private static final Integer grade = 10;
    private static final String comment = "Comment";
    private CoachGradeDTO gradeRequest;

    @BeforeEach
    void init() {
        gradeRequest = CoachGradeDTO.builder()
                .testId(GOOD_TEST_ID)
                .questionId(questionId)
                .grade(grade)
                .comment(comment)
                .build();
    }

    @Test
    void getGradesTestNotFound() {
        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.getGrades(GOOD_TEST_ID));
    }

    @Test
    void getGradesSuccess() {
        Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);
        Mockito.when(question.getId()).thenReturn(questionId);

        Mockito.when(coachGrade.getId()).thenReturn(testQuestionID);

        Mockito.when(testQuestionID.getTest()).thenReturn(test);
        Mockito.when(testQuestionID.getQuestion()).thenReturn(question);
        Mockito.when(coachGrade.getGrade()).thenReturn(grade);

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);
        Mockito.when(gradeService.getGradesByTest(test)).thenReturn(Collections.singletonList(coachGrade));

        List<CoachGradeDTO> grades = gradeController.getGrades(GOOD_TEST_ID);
        Assertions.assertEquals(1, grades.size());
        Assertions.assertEquals(GOOD_TEST_ID, grades.get(0).getTestId());
        Assertions.assertEquals(questionId, grades.get(0).getQuestionId());
        Assertions.assertEquals(grade, grades.get(0).getGrade());
    }


    @org.junit.jupiter.api.Test
    void coachSubmitSuccess() {
        gradeController.coachSubmit(GOOD_TEST_ID);

        Mockito.verify(testsService).coachSubmit(GOOD_TEST_ID);
    }

    @org.junit.jupiter.api.Test
    void coachSubmitFail() {
        Mockito.doThrow(TestNotFoundException.class).when(testsService).coachSubmit(BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.coachSubmit(BAD_TEST_ID));
    }

    @Test
    void addGradeSuccess() {
        CoachGradeDTO gradeDTO = CoachGradeDTO.builder()
                .testId(GOOD_TEST_ID)
                .questionId(questionId)
                .grade(grade)
                .comment(comment)
                .build();
        gradeController.add(gradeDTO);

        Mockito.verify(gradeService).add(GOOD_TEST_ID, questionId, grade, comment);
    }

    @Test
    void addGradeTestNotFound() {
        Mockito.doThrow(TestNotFoundException.class)
                .when(gradeService).add(GOOD_TEST_ID, questionId, grade, comment);
        Assertions.assertThrows(TestNotFoundException.class, () -> gradeController.add(gradeRequest));
    }

    @Test
    void addGradeQuestionNotFound() {
        Mockito.doThrow(QuestionNotFoundException.class)
                .when(gradeService).add(GOOD_TEST_ID, questionId, grade, comment);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> gradeController.add(gradeRequest));
    }
}
