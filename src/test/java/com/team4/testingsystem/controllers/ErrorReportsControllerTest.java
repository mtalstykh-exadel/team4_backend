package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
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
import org.mockito.junit.jupiter.MockitoExtension;

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
    TestQuestionID testQuestionID;

    @InjectMocks
    ErrorReportsController errorReportsController;

    @Test
    void addSuccess() {
        errorReportsController.add(EntityCreatorUtil.createErrorReportDTO(
                GOOD_REPORT_BODY,
                GOOD_QUESTION_ID,
                GOOD_TEST_ID));
        verify(errorReportsService).add(GOOD_REPORT_BODY, GOOD_QUESTION_ID, GOOD_TEST_ID);
    }

    @Test
    void addFailQuestionNotFound() {

        doThrow(QuestionNotFoundException.class).when(errorReportsService)
                .add(BAD_REPORT_BODY, BAD_QUESTION_ID, GOOD_TEST_ID);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsController.add(EntityCreatorUtil
                        .createErrorReportDTO(BAD_REPORT_BODY, BAD_QUESTION_ID, GOOD_TEST_ID)));
    }

    @Test
    void addFailTestNotFound() {
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
    void removeFailTestNotFound() {

        doThrow(TestNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID);

        Assertions.assertThrows(TestNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID));
    }

    @Test
    void removeFailQuestionNotFound() {

        doThrow(QuestionNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID);

        Assertions.assertThrows(QuestionNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID));
    }

    @Test
    void removeFailErrorReportNotFound() {

        doThrow(ErrorReportNotFoundException.class).when(errorReportsService)
                .removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID);

        Assertions.assertThrows(ErrorReportNotFoundException.class,
                ()-> errorReportsController.removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID));
    }
}
