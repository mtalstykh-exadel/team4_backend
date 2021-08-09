package com.team4.testingsystem.services.impl;


import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ModuleGradesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TestEvaluationServiceImplTest {
    @Mock
    private ChosenOptionService chosenOptionService;

    @Mock
    private ModuleGradesService moduleGradesService;

    @Mock
    private CoachGradeRepository coachGradeRepository;

    @InjectMocks
    private TestEvaluationServiceImpl testEvaluationService;

    @Mock
    private Stream<ChosenOption> streamChosenOption;

    @Mock
    private Stream<Answer> streamAnswer;

    @Mock
    private Stream<CoachGrade> streamCoachGrade;

    @Mock
    private List<ChosenOption> chosenOptionList;

    @Mock
    private List<CoachGrade> coachGrades;

    @Mock
    private CoachGrade coachGrade;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Test
    public void getEvaluationBeforeCoachCheckSuccess() {

        Mockito.when(chosenOptionService.getChosenOptionByTest(test)).thenReturn(chosenOptionList);
        Mockito.when(chosenOptionList.stream()).thenReturn(streamChosenOption);
        Mockito.when(streamChosenOption.filter(any())).thenReturn(streamChosenOption);
        Mockito.when(streamChosenOption.map(any())).thenAnswer(invocation -> streamAnswer);
        Mockito.when(streamAnswer.filter(any())).thenReturn(streamAnswer);
        Mockito.when(streamAnswer.count()).thenReturn(10L);

        int evaluation = testEvaluationService.getEvaluationBeforeCoachCheck(test);

        verify(moduleGradesService).add(test, "Essay", 0);

        verify(moduleGradesService).add(test, "Speaking", 0);

        verify(moduleGradesService).add(test, "Grammar", 10);

        verify(moduleGradesService).add(test, "Listening", 10);

        Assertions.assertEquals(20, evaluation);
    }
    @Test
    public void getEvaluationAfterCoachCheckSuccess() {
        Mockito.when(test.getEvaluation()).thenReturn(20);

        Mockito.when(coachGradeRepository.findAllById_Test(test)).thenReturn(coachGrades);

        Mockito.when(coachGrades.stream()).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.filter(any())).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.findAny()).thenReturn(Optional.of(coachGrade));

        Mockito.when(coachGrade.getGrade()).thenReturn(10);

        int evaluation = testEvaluationService.getEvaluationAfterCoachCheck(test);

        verify(moduleGradesService).add(test, "Essay", 10);

        verify(moduleGradesService).add(test, "Speaking", 10);

        Assertions.assertEquals(40, evaluation);
    }

    @Test
    public void getEvaluationAfterCoachCheckFail() {
        Mockito.when(test.getEvaluation()).thenReturn(20);

        Mockito.when(coachGradeRepository.findAllById_Test(test)).thenReturn(coachGrades);

        Mockito.when(coachGrades.stream()).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.filter(any())).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.findAny()).thenReturn(Optional.empty());

        Assertions.assertThrows(CoachGradeNotFoundException.class,
                ()->testEvaluationService.getEvaluationAfterCoachCheck(test));

    }
}

