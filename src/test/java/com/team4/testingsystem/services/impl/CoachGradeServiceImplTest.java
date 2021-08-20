package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
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

    @Mock
    private RestrictionsService restrictionsService;

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
    void getGradesByTestSuccess() {
        ArrayList<CoachGrade> grades = new ArrayList<>();
        Mockito.when(gradeRepository.findAllById_Test(test)).thenReturn(grades);
        gradeService.getGradesByTest(test);

        Mockito.verify(restrictionsService).checkStatus(test, Status.IN_VERIFICATION);
        Mockito.verify(restrictionsService).checkCoachIsCurrentUser(test);
        Assertions.assertEquals(grades, gradeService.getGradesByTest(test));
    }

    @Test
    void addGradeSuccess() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getById(questionId)).thenReturn(question);

        gradeService.add(testId, questionId, grade, "Comment");

        Mockito.verify(restrictionsService).checkStatus(test, Status.IN_VERIFICATION);
        Mockito.verify(restrictionsService).checkCoachIsCurrentUser(test);
        Mockito.verify(restrictionsService).checkTestContainsQuestion(test, question);
        Mockito.verify(restrictionsService).checkModuleIsEssayOrSpeaking(question);
        Mockito.verify(restrictionsService).checkGradeIsCorrect(grade);
        Mockito.verify(gradeRepository).save(any());

    }

    @Test
    void addGradeTestNotFound() {
        Mockito.when(testsService.getById(testId)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> gradeService.add(testId, questionId, grade, null));
    }

    @Test
    void addGradeQuestionNotFound() {
        Mockito.when(testsService.getById(testId)).thenReturn(test);
        Mockito.when(questionService.getById(questionId)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> gradeService.add(testId, questionId, grade, null));
    }
}
