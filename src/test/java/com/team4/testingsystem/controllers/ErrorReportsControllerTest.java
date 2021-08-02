package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.ErrorReportId;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ErrorReportsControllerTest {

    final long GOOD_QUESTION_ID = 11L;

    final long BAD_QUESTION_ID = 4242L;

    final long GOOD_TEST_ID = 111L;

    final long BAD_TEST_ID = 424242L;

    final String GOOD_REPORT_BODY = "This question is 2 difficult 4 me!1!1!1";

    final String BAD_REPORT_BODY = "I have no idea what to write here";

    @Mock
    ErrorReportsService errorReportsService;

    @Mock
    com.team4.testingsystem.entities.Test test;

    @Mock
    Question question;

    @Mock
    ErrorReportId errorReportId;

    @Mock
    ErrorReport errorReport;

    @InjectMocks
    ErrorReportsController errorReportsController;

    @Test
    void getAllSuccess() {

        Mockito.when(test.getId()).thenReturn(GOOD_TEST_ID);
        Mockito.when(question.getId()).thenReturn(GOOD_QUESTION_ID);

        Mockito.when(errorReport.getErrorReportId()).thenReturn(errorReportId);

        Mockito.when(errorReportId.getTest()).thenReturn(test);

        Mockito.when(errorReportId.getQuestion()).thenReturn(question);

        Mockito.when(errorReport.getReportBody()).thenReturn(GOOD_REPORT_BODY);

        Mockito.when(errorReportsService.getReportsByTest(GOOD_TEST_ID))
                .thenReturn(Collections.singletonList(errorReport));

        List<ErrorReportDTO> reports = errorReportsController.getReports(GOOD_TEST_ID);

        Assertions.assertEquals(1, reports.size());
        Assertions.assertEquals(GOOD_TEST_ID, reports.get(0).getTestId());
        Assertions.assertEquals(GOOD_QUESTION_ID, reports.get(0).getQuestionId());
        Assertions.assertEquals(GOOD_REPORT_BODY, reports.get(0).getReportBody());
    }

    @Test
    void getAllFail() {
        Mockito.when(errorReportsService.getReportsByTest(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);
        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsController.getReports(BAD_TEST_ID));
    }

    @Test
    void addSuccess() {
        errorReportsController.add(EntityCreatorUtil.createErrorReportDTO(
                GOOD_REPORT_BODY,
                GOOD_QUESTION_ID,
                GOOD_TEST_ID));
        verify(errorReportsService).add(GOOD_REPORT_BODY, GOOD_QUESTION_ID, GOOD_TEST_ID);
    }

    @Test
    void addFailFirst() {

        doThrow(QuestionNotFoundException.class).when(errorReportsService)
                .add(BAD_REPORT_BODY, BAD_QUESTION_ID, GOOD_TEST_ID);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsController.add(EntityCreatorUtil
                        .createErrorReportDTO(BAD_REPORT_BODY, BAD_QUESTION_ID, GOOD_TEST_ID)));
    }

    @Test
    void addFailSecond() {
        doThrow(TestNotFoundException.class).when(errorReportsService)
                .add(BAD_REPORT_BODY, GOOD_QUESTION_ID, BAD_TEST_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsController.add(EntityCreatorUtil
                        .createErrorReportDTO(BAD_REPORT_BODY, GOOD_QUESTION_ID, BAD_TEST_ID)));
    }

    @Test
    void removeSuccess() {

        errorReportsController.removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID);

        verify(errorReportsService).removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID);
    }

    @Test
    void removeFailFirst() {

        doThrow(TestNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID));
    }

    @Test
    void removeFailSecond() {

        doThrow(QuestionNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID);

        Assertions.assertThrows(QuestionNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID));
    }

    @Test
    void removeFailThird() {

        doThrow(ErrorReportNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID);

        Assertions.assertThrows(ErrorReportNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID));
    }
}
