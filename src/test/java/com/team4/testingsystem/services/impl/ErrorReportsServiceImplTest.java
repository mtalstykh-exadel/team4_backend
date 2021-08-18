package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.TestQuestionID;
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
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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


    @InjectMocks
    ErrorReportsServiceImpl errorReportsService;

    @Test
    void addSuccess() {
        Mockito.when(questionService.getById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

            errorReportsService.add("Good report", GOOD_QUESTION_ID, GOOD_TEST_ID);
            verify(errorReportsRepository).save(any());
    }


    @Test
    void addFailQuestionNotFound() {
        Mockito.when(questionService.getById(BAD_QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.add("Bad report", BAD_QUESTION_ID, GOOD_TEST_ID));
    }

    @Test
    void addFailTestNotFound() {
        Mockito.when(questionService.getById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.add("Bad report", GOOD_QUESTION_ID, BAD_TEST_ID));
    }

    @Test
    void addAll(){
        ErrorReport errorReport = new ErrorReport();
        Assertions.assertDoesNotThrow(()->errorReportsService.addAll(List.of(errorReport)));
        verify(errorReportsRepository).saveAll(List.of(errorReport));
    }

    @Test
    void removeSuccess() {

        Mockito.when(testsService.getById(GOOD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getById(GOOD_QUESTION_ID)).thenReturn(question);

        try (MockedConstruction<TestQuestionID> mockedConstruction = Mockito.mockConstruction(TestQuestionID.class,
                (mock, context) -> {
                    Mockito.when(errorReportsRepository.removeById(mock)).thenReturn(1);

                })) {

            errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID);

            verify(errorReportsRepository).removeById(any());

            Assertions.assertDoesNotThrow(() -> errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, GOOD_QUESTION_ID));

        }


    }

    @Test
    void removeFailQuestionNotFound() {
        Mockito.when(questionService.getById(BAD_QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.removeByTestAndQuestion(GOOD_TEST_ID, BAD_QUESTION_ID));
    }

    @Test
    void removeFailTestNotFound() {

        Mockito.when(questionService.getById(GOOD_QUESTION_ID)).thenReturn(question);

        Mockito.when(testsService.getById(BAD_TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.removeByTestAndQuestion(BAD_TEST_ID, GOOD_QUESTION_ID));
    }


    @Test
    void removeFailErrorReportNotFound() {
        Mockito.when(testsService.getById(BAD_TEST_ID)).thenReturn(test);

        Mockito.when(questionService.getById(BAD_QUESTION_ID)).thenReturn(question);


        try (MockedConstruction<TestQuestionID> mockedConstruction = Mockito.mockConstruction(TestQuestionID.class,
                (mock, context) -> {
                    //Error report doesn't exist
                    Mockito.when(errorReportsRepository.removeById(mock)).thenReturn(0);
                })) {
            Assertions.assertThrows(ErrorReportNotFoundException.class,
                    () -> errorReportsService.removeByTestAndQuestion(BAD_TEST_ID, BAD_QUESTION_ID));
        }
    }
}
