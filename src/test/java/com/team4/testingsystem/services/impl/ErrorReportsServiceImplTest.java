package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.repositories.TestsRepository;
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


    @Mock
    ErrorReportsRepository errorReportsRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    TestsRepository testsRepository;

    @Mock
    private Question question;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private ErrorReport errorReport;

    @InjectMocks
    ErrorReportsServiceImpl errorReportsService;

    @Test
    void getAllSuccess(){
        List<ErrorReport> errorReports = new ArrayList<>();
        Mockito.when(errorReportsRepository.findAll()).thenReturn(errorReports);

        Assertions.assertEquals(errorReports, errorReportsService.getAll());
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(errorReportsRepository.findById(1L)).thenReturn(Optional.of(errorReport));

        Assertions.assertEquals(errorReport, errorReportsService.getById(1L));
    }

    @Test
    void getByIdFail() {

        Mockito.when(errorReportsRepository.findById(42L)).thenThrow(ErrorReportNotFoundException.class);

        Assertions.assertThrows(ErrorReportNotFoundException.class, () -> errorReportsService.getById(42L));
    }

    @Test
    void addSuccess(){
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Mockito.when(testsRepository.findById(1L)).thenReturn(Optional.of(test));

        errorReportsService.add("Good report", 1L, 1L);

        verify(errorReportsRepository).save(any());
    }

    @Test
    void addFailFirst(){
        Mockito.when(questionRepository.findById(42L)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> errorReportsService.add("Bad report",42L, 42L));
    }

    @Test
    void addFailSecond(){
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Mockito.when(testsRepository.findById(42L)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> errorReportsService.add("Bad report",1L, 42L));
    }

    @Test
    void updateRequestBodySuccess(){
        Mockito.when(errorReportsRepository.changeReportBody("Good report", 1L)).thenReturn(1);

        errorReportsService.updateRequestBody(1L, "Good report");

        verify(errorReportsRepository).changeReportBody("Good report", 1L);

        Assertions.assertDoesNotThrow(()->errorReportsService.updateRequestBody(1L, "Good report"));
    }

    @Test
    void updateRequestBodyFail(){
        Mockito.when(errorReportsRepository.changeReportBody("Bad report", 42L)).thenReturn(0);

        Assertions.assertThrows(ErrorReportNotFoundException.class,
                () -> errorReportsService.updateRequestBody(42L, "Bad report"));
    }

    @Test
    void removeSuccess() {

        Mockito.when(errorReportsRepository.removeById(1L)).thenReturn(1);

        errorReportsService.removeById(1L);

        verify(errorReportsRepository).removeById(1L);

        Assertions.assertDoesNotThrow(()->errorReportsService.removeById(1L));

    }

    @Test
    void removeFail() {

        Mockito.when(errorReportsRepository.removeById(42L)).thenReturn(0);

        Assertions.assertThrows(ErrorReportNotFoundException.class, () -> errorReportsService.removeById(42L));

    }
}
