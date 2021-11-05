package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.CoachAnswer;
import com.team4.testingsystem.model.entity.ErrorReport;
import com.team4.testingsystem.model.entity.ErrorReportAnswer;
import com.team4.testingsystem.model.entity.TestQuestionID;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ErrorReportAnswerServiceImplTest {

    @Mock
    private CoachAnswerRepository coachAnswerRepository;

    @Mock
    private ErrorReportsRepository errorReportsRepository;

    @InjectMocks
    private ErrorReportAnswerServiceImpl errorReportAnswerService;

    @Mock
    private CoachAnswer coachAnswer;

    @Mock
    private ErrorReport errorReport;

    @Mock
    private TestQuestionID testQuestionID;

    private static final Long TEST_ID = 1L;

    @Test
    void getReportsWithAnswersByTestSuccess() {
        Mockito.when(coachAnswerRepository.findAllByTestId(TEST_ID)).thenReturn(List.of(coachAnswer));
        Mockito.when(errorReportsRepository.findAllByTestId(TEST_ID)).thenReturn(List.of(errorReport));

        Mockito.when(coachAnswer.getId()).thenReturn(testQuestionID);
        Mockito.when(errorReport.getId()).thenReturn(testQuestionID);

        List<ErrorReportAnswer> reportAnswers = errorReportAnswerService.getReportsWithAnswersByTest(TEST_ID);

        Assertions.assertEquals(1, reportAnswers.size());
        Assertions.assertEquals(errorReport, reportAnswers.get(0).getReport());
        Assertions.assertEquals(coachAnswer, reportAnswers.get(0).getAnswer());
    }
}
