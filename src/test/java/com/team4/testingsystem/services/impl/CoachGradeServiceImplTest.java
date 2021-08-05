package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

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
    private Question question;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    private final Long testId = 1L;
    private final Long questionId = 2L;
    private final Integer grade = 10;

    @Test
    void getGradesByTestNotFound() {
        Mockito.when(testsService.getById(1L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class, () -> gradeService.getGradesByTest(1L));
    }

    @Test
    void getGradesByTestSuccess() {
        Mockito.when(testsService.getById(1L)).thenReturn(test);
        ArrayList<CoachGrade> grades = new ArrayList<>();
        Mockito.when(gradeRepository.findAllById_Test(test)).thenReturn(grades);

        Assertions.assertEquals(grades, gradeService.getGradesByTest(1L));
    }


    @Test
    void addGradeSuccess() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getById(questionId)).thenReturn(question);


        gradeService.add(testId, questionId, grade);

        Mockito.verify(gradeRepository).save(any());

    }

    @Test
    void createGradeTestNotFound() {
        Mockito.when(testsService.getById(testId)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> gradeService.add(testId, questionId, grade));
    }

    @Test
    void addGradeQuestionNotFound() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getById(questionId)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> gradeService.add(testId, questionId, grade));
    }


}
