package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
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
    void createGradeSuccess() {
        gradeService.createGrade(test, question, 10);

        ArgumentCaptor<CoachGrade> captor = ArgumentCaptor.forClass(CoachGrade.class);
        Mockito.verify(gradeRepository).save(captor.capture());

        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertEquals(question, captor.getValue().getQuestion());
        Assertions.assertEquals(10, captor.getValue().getGrade());
    }

    @Test
    void updateGradeNotExists() {
        Mockito.when(gradeRepository.updateGrade(1L, 1L, 10)).thenReturn(0);

        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.updateGrade(1L, 1L, 10));
    }

    @Test
    void updateGradeSuccess() {
        Mockito.when(gradeRepository.updateGrade(1L, 1L, 10)).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> gradeService.updateGrade(1L, 1L, 10));
    }
}
