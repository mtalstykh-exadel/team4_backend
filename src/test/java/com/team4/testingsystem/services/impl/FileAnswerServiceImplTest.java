package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileAnswerServiceImplTest {

    @Mock
    private TestsService testsService;

    @Mock
    private QuestionService questionService;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Mock
    private FileAnswer fileAnswer;

    @Mock
    private Test test;

    @Mock
    private Question question;

    @InjectMocks
    private FileAnswerServiceImpl fileAnswerService;

    private static final Long TEST_ID = 1L;
    private static final Long QUESTION_ID = 2L;
    private static final String URL = "url";

    @org.junit.jupiter.api.Test
    void getUrlNotFound() {
        Mockito.when(fileAnswerRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(FileAnswerNotFoundException.class,
                () -> fileAnswerService.getUrl(TEST_ID, QUESTION_ID));
    }

    @org.junit.jupiter.api.Test
    void getUrlSuccess() {
        Mockito.when(fileAnswerRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
                .thenReturn(Optional.of(fileAnswer));
        Mockito.when(fileAnswer.getUrl()).thenReturn(URL);

        Assertions.assertEquals(URL, fileAnswerService.getUrl(TEST_ID, QUESTION_ID));
    }

    @org.junit.jupiter.api.Test
    void saveTestNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> fileAnswerService.save(TEST_ID, QUESTION_ID, URL));
    }

    @org.junit.jupiter.api.Test
    void saveQuestionNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> fileAnswerService.save(TEST_ID, QUESTION_ID, URL));
    }

    @org.junit.jupiter.api.Test
    void saveQuestionSuccess() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenReturn(question);

        fileAnswerService.save(TEST_ID, QUESTION_ID, URL);

        ArgumentCaptor<FileAnswer> captor = ArgumentCaptor.forClass(FileAnswer.class);
        Mockito.verify(fileAnswerRepository).save(captor.capture());

        Assertions.assertEquals(test, captor.getValue().getId().getTest());
        Assertions.assertEquals(question, captor.getValue().getId().getQuestion());
        Assertions.assertEquals(URL, captor.getValue().getUrl());
    }

    @org.junit.jupiter.api.Test
    void removeTestNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> fileAnswerService.remove(TEST_ID, QUESTION_ID));
    }

    @org.junit.jupiter.api.Test
    void removeQuestionNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> fileAnswerService.remove(TEST_ID, QUESTION_ID));
    }

    @org.junit.jupiter.api.Test
    void removeSuccess() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getById(QUESTION_ID)).thenReturn(question);

        fileAnswerService.remove(TEST_ID, QUESTION_ID);

        ArgumentCaptor<TestQuestionID> captor = ArgumentCaptor.forClass(TestQuestionID.class);
        Mockito.verify(fileAnswerRepository).deleteById(captor.capture());

        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertEquals(question, captor.getValue().getQuestion());
    }
}
