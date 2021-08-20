package com.team4.testingsystem.services.impl;


import com.team4.testingsystem.entities.*;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.CoachGradeNotFoundException;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.repositories.CoachGradeRepository;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TestEvaluationServiceImplTest {

    private final int ESSAY_SCORE_BEFORE_COACH_CHECK = 0;

    private final int SPEAKING_SCORE_BEFORE_COACH_CHECK = 0;

    private final int GRAMMAR_SCORE = 10;

    private final int LISTENING_SCORE = 10;

    private final int ESSAY_SCORE = 8;

    private final int SPEAKING_SCORE = 9;

    @Mock
    private ChosenOptionService chosenOptionService;

    @Mock
    private ModuleGradesService moduleGradesService;

    @Mock
    private CoachGradeRepository coachGradeRepository;

    @Mock
    private CoachAnswerRepository coachAnswerRepository;

    @InjectMocks
    private TestEvaluationServiceImpl testEvaluationService;

    @Mock
    private Stream<ChosenOption> streamChosenOption;

    @Mock
    private Stream<Answer> streamAnswer;

    @Mock
    private Stream<CoachGrade> streamCoachGrade;

    @Mock
    private Stream<CoachAnswer> streamCoachAnswer;

    @Mock
    private List<CoachAnswer> filteredCoachAnswers;

    @Mock
    private List<ChosenOption> chosenOptionList;

    @Mock
    private List<CoachGrade> coachGrades;

    @Mock
    private List<CoachAnswer> coachAnswers;

    @Mock
    private CoachGrade coachGrade;

    @Mock
    private Map<String, CoachGrade> gradeMap;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    private static final Long TEST_ID = 1L;

    @Test
    public void countScoreBeforeCoachCheckSuccess() {

        Mockito.when(test.getId()).thenReturn(TEST_ID);
        Mockito.when(chosenOptionService.getAllByTestId(TEST_ID)).thenReturn(chosenOptionList);
        Mockito.when(chosenOptionList.stream()).thenReturn(streamChosenOption);
        Mockito.when(streamChosenOption.filter(any())).thenReturn(streamChosenOption);
        Mockito.when(streamChosenOption.map(any())).thenAnswer(invocation -> streamAnswer);
        Mockito.when(streamAnswer.filter(any())).thenReturn(streamAnswer);
        Mockito.when(streamAnswer.count()).thenReturn(10L);

        testEvaluationService.countScoreBeforeCoachCheck(test);

        verify(moduleGradesService)
                .add(test, Modules.ESSAY.getName(), ESSAY_SCORE_BEFORE_COACH_CHECK, null);

        verify(moduleGradesService)
                .add(test, Modules.SPEAKING.getName(), SPEAKING_SCORE_BEFORE_COACH_CHECK, null);

        verify(moduleGradesService).add(test, Modules.GRAMMAR.getName(), GRAMMAR_SCORE, null);

        verify(moduleGradesService).add(test, Modules.LISTENING.getName(), LISTENING_SCORE, null);

    }
    @Test
    public void updateScoreAfterCoachCheckSuccess() {

        Mockito.when(coachGradeRepository.findAllById_Test(test)).thenReturn(coachGrades);

        Mockito.when(coachGrades.stream()).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.collect(any())).thenReturn(gradeMap);

        Mockito.when(gradeMap.get(Modules.ESSAY.getName())).thenReturn(coachGrade);

        Mockito.when(gradeMap.get(Modules.SPEAKING.getName())).thenReturn(coachGrade);

        Mockito.when(coachGrade.getGrade()).thenReturn(ESSAY_SCORE);

        Mockito.when(coachGrade.getComment()).thenReturn("Cool");

        testEvaluationService.updateScoreAfterCoachCheck(test);

        verify(moduleGradesService).add(test, Modules.ESSAY.getName(), ESSAY_SCORE, "Cool");

        verify(moduleGradesService).add(test, Modules.SPEAKING.getName(), ESSAY_SCORE, "Cool");
    }

    @Test
    public void getEvaluationAfterCoachCheckFail() {

        Mockito.when(coachGradeRepository.findAllById_Test(test)).thenReturn(coachGrades);

        Mockito.when(coachGrades.stream()).thenReturn(streamCoachGrade);

        Mockito.when(streamCoachGrade.collect(any())).thenReturn(gradeMap);

        Mockito.when(gradeMap.get(Modules.ESSAY.getName())).thenReturn(null);

        coachAnswers.add(new CoachAnswer(
                new TestQuestionID(test, EntityCreatorUtil.createQuestion()), "comment"));

        Mockito.when(coachAnswerRepository.findAllById_Test(test)).thenReturn(coachAnswers);

        Assertions.assertThrows(CoachGradeNotFoundException.class,
                ()->testEvaluationService.updateScoreAfterCoachCheck(test));
    }

}

