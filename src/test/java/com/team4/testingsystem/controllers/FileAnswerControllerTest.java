package com.team4.testingsystem.controllers;

import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.services.FileAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileAnswerControllerTest {
    @Mock
    private FileAnswerService fileAnswerService;

    @InjectMocks
    private FileAnswerController fileAnswerController;

    private static final Long TEST_ID = 1L;
    private static final String ESSAY_TEXT = "text";

    @Test
    void downloadEssayQuestionNotFound() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> fileAnswerController.downloadEssay(TEST_ID));
    }

    @Test
    void downloadEssayFileAnswerNotFound() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenThrow(FileAnswerNotFoundException.class);

        Assertions.assertThrows(FileAnswerNotFoundException.class,
                () -> fileAnswerController.downloadEssay(TEST_ID));
    }

    @Test
    void downloadEssayLoadingError() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenThrow(FileLoadingFailedException.class);

        Assertions.assertThrows(FileLoadingFailedException.class,
                () -> fileAnswerController.downloadEssay(TEST_ID));
    }

    @Test
    void downloadEssaySuccess() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenReturn(ESSAY_TEXT);

        Assertions.assertEquals(ESSAY_TEXT, fileAnswerController.downloadEssay(TEST_ID));
    }

    @Test
    void uploadEssayTestNotFound() {
        Mockito.doThrow(TestNotFoundException.class)
                .when(fileAnswerService).uploadEssay(TEST_ID, ESSAY_TEXT);

        Assertions.assertThrows(TestNotFoundException.class,
                () -> fileAnswerController.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @Test
    void uploadEssayQuestionNotFound() {
        Mockito.doThrow(QuestionNotFoundException.class)
                .when(fileAnswerService).uploadEssay(TEST_ID, ESSAY_TEXT);

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> fileAnswerController.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @Test
    void uploadEssaySavingError() {
        Mockito.doThrow(FileSavingFailedException.class)
                .when(fileAnswerService).uploadEssay(TEST_ID, ESSAY_TEXT);

        Assertions.assertThrows(FileSavingFailedException.class,
                () -> fileAnswerController.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @Test
    void uploadEssaySuccess() {
        fileAnswerController.uploadEssay(TEST_ID, ESSAY_TEXT);
        Mockito.verify(fileAnswerService).uploadEssay(TEST_ID, ESSAY_TEXT);
    }
}
