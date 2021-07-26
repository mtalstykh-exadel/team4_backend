package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.GradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CoachGradeServiceImplTest {
    @Mock
    private CoachGradeRepository gradeRepository;

    @InjectMocks
    private CoachGradeServiceImpl gradeService;

    @Mock
    private CoachGrade coachGrade;

    @Mock
    private Question question;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Test
    void getByIdNotExists() {
        Mockito.when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.getGradeById(1L));
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(gradeRepository.findById(1L)).thenReturn(Optional.of(coachGrade));
        Assertions.assertEquals(coachGrade, gradeService.getGradeById(1L));
    }

    @Test
    void getByQuestionNotExists() {
        Mockito.when(gradeRepository.findByQuestion(question)).thenReturn(Optional.empty());
        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.getGradeByQuestion(question));
    }

    @Test
    void getByQuestionSuccess() {
        Mockito.when(gradeRepository.findByQuestion(question)).thenReturn(Optional.of(coachGrade));
        Assertions.assertEquals(coachGrade, gradeService.getGradeByQuestion(question));
    }

    @Test
    void createGradeSuccess() {
        gradeService.createGrade(question, test, 10);

        ArgumentCaptor<CoachGrade> captor = ArgumentCaptor.forClass(CoachGrade.class);
        Mockito.verify(gradeRepository).save(captor.capture());

        Assertions.assertEquals(question, captor.getValue().getQuestion());
        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertEquals(10, captor.getValue().getGrade());
    }

    @Test
    void updateGradeNotExists() {
        Mockito.when(gradeRepository.updateGrade(1L, 10)).thenReturn(0);
        Assertions.assertThrows(GradeNotFoundException.class, () -> gradeService.updateGrade(1L, 10));
    }

    @Test
    void updateGradeSuccess() {
        Mockito.when(gradeRepository.updateGrade(1L, 10)).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> gradeService.updateGrade(1L, 10));
    }
}
