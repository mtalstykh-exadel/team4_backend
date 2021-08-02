package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.ErrorReportAlreadyExistsException;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ErrorReportsServiceImplTest {


    final Long GOOD_TEST_ID = 1L;

    final Long BAD_TEST_ID = 42L;

    final Long GOOD_QUESTION_ID = 111L;

    final Long BAD_QUESTION_ID = 424242L;

    @Mock
    ErrorReportsRepository errorReportsRepository;

    @Mock
    QuestionService questionService;

    @Mock
    TestsService testsService;

    @Mock
    private Question question;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private ErrorReport errorReport;

    @InjectMocks
    ErrorReportsServiceImpl errorReportsService;

    @Test
    void getByTestSuccess(){
        List<ErrorReport> errorReports = new ArrayList<>();

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(errorReportsRepository.findAllByTest(test)).thenReturn(errorReports);

        Assertions.assertEquals(errorReports, errorReportsService.getReportsByTest(GOOD_TEST_ID));
    }

    @Test
    void getByTestFail(){

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.getReportsByTest(BAD_TEST_ID));
    }

    @Test
    void addSuccess(){
        Mockito.when(questionService.getQuestionById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(errorReportsRepository.findByTestAndQuestion(test,question)).thenReturn(Optional.empty());

        errorReportsService.add("Good report", GOOD_QUESTION_ID, GOOD_TEST_ID);

        verify(errorReportsRepository).save(any());
    }

    @Test
    void addFailFirst(){
        Mockito.when(questionService.getQuestionById(BAD_QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.add("Bad report",BAD_QUESTION_ID, GOOD_TEST_ID));
    }

    @Test
    void addFailSecond(){
        Mockito.when(questionService.getQuestionById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.add("Bad report", GOOD_QUESTION_ID, BAD_TEST_ID));
    }

    @Test
    void addFailThird(){
        Mockito.when(questionService.getQuestionById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(errorReportsRepository.findByTestAndQuestion(test,question)).thenReturn(Optional.of(errorReport));

        Assertions.assertThrows(ErrorReportAlreadyExistsException.class,
                () -> errorReportsService.add("Good report", GOOD_QUESTION_ID, GOOD_TEST_ID));
    }


    @Test
    void updateRequestBodySuccess(){
        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(errorReportsRepository.changeReportBody("Good report", test, question)).thenReturn(1);

        errorReportsService.updateReportBody(GOOD_TEST_ID, GOOD_QUESTION_ID, "Good report");

        verify(errorReportsRepository).changeReportBody("Good report", test, question);

        Assertions.assertDoesNotThrow(
                ()->errorReportsService.updateReportBody(GOOD_TEST_ID, GOOD_QUESTION_ID,"Good report"));
    }

    @Test
    void updateRequestBodyFailFirst(){
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.updateReportBody(BAD_TEST_ID, GOOD_QUESTION_ID, "Bad report"));
    }

    @Test
    void updateRequestBodyFailSecond(){

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(BAD_QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.updateReportBody(GOOD_TEST_ID, BAD_QUESTION_ID, "Bad report"));
    }


    @Test
    void updateRequestBodyFailThird(){
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(BAD_QUESTION_ID)).thenReturn(question);

        //Error report doesn't exist
        Mockito.when(errorReportsRepository.changeReportBody("Bad report", test, question)).thenReturn(0);

        Assertions.assertThrows(ErrorReportNotFoundException.class,
                () -> errorReportsService.updateReportBody(BAD_TEST_ID, BAD_QUESTION_ID, "Bad report"));
    }

    @Test
    void removeSuccess() {

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(errorReportsRepository.removeByTestAndQuestion(test, question)).thenReturn(1);

        errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID);

        verify(errorReportsRepository).removeByTestAndQuestion(test, question);

        Assertions.assertDoesNotThrow(()->errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID));

    }

    @Test
    void removeFailFirst(){
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID));
    }

    @Test
    void removeFailSecond(){

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(BAD_QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID));
    }


    @Test
    void removeFailThird(){
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getQuestionById(BAD_QUESTION_ID)).thenReturn(question);

        //Error report doesn't exist
        Mockito.when(errorReportsRepository.removeByTestAndQuestion(test, question)).thenReturn(0);

        Assertions.assertThrows(ErrorReportNotFoundException.class,
                () -> errorReportsService.removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID));
    }
}
