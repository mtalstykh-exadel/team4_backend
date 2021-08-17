package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TooLongEssayException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileAnswerServiceImplTest {

    @Mock
    private TestsService testsService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ResourceStorageService resourceStorageService;

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
    private static final String ESSAY_TEXT = "essay text example";

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

    @org.junit.jupiter.api.Test
    void downloadSpeakingSuccess() {
        Mockito.when(questionService
            .getQuestionByTestIdAndModule(any(), any())).thenReturn(question);
        Mockito.when(fileAnswerRepository
            .findByTestAndQuestionId(any(), any())).thenReturn(Optional.of(fileAnswer));
        Mockito.when(fileAnswerService.getUrl(any(), any())).thenReturn(URL);
        Assertions.assertEquals(URL, fileAnswerService.downloadSpeaking(EntityCreatorUtil.ID));
    }

    @org.junit.jupiter.api.Test
    void downloadSpeakingQuestionNotFound() {
        Mockito.doThrow(QuestionNotFoundException.class)
            .when(questionService).getQuestionByTestIdAndModule(TEST_ID, Modules.SPEAKING);
        Assertions.assertThrows(QuestionNotFoundException.class,
            () -> fileAnswerService.downloadSpeaking(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void downloadSpeakingFileAnswerNotFound() {
        Mockito.doThrow(FileAnswerNotFoundException.class)
            .when(questionService).getQuestionByTestIdAndModule(TEST_ID, Modules.SPEAKING);
        Assertions.assertThrows(FileAnswerNotFoundException.class,
            () -> fileAnswerService.downloadSpeaking(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void downloadEssayQuestionNotFound() {
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY))
            .thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
            () -> fileAnswerService.downloadEssay(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void downloadEssayFileAnswerNotFound() {
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY))
            .thenReturn(question);
        Mockito.when(question.getId()).thenReturn(QUESTION_ID);
        Mockito.when(fileAnswerRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(FileAnswerNotFoundException.class,
            () -> fileAnswerService.downloadEssay(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void downloadEssayLoadingError() {
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY))
            .thenReturn(question);
        Mockito.when(question.getId()).thenReturn(QUESTION_ID);
        Mockito.when(fileAnswerRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
            .thenReturn(Optional.of(fileAnswer));
        Mockito.when(fileAnswer.getUrl()).thenReturn(URL);

        Mockito.when(resourceStorageService.load(URL)).thenThrow(FileLoadingFailedException.class);

        Assertions.assertThrows(FileLoadingFailedException.class,
            () -> fileAnswerService.downloadEssay(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void downloadEssaySuccess() {
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY))
            .thenReturn(question);
        Mockito.when(question.getId()).thenReturn(QUESTION_ID);
        Mockito.when(fileAnswerRepository.findByTestAndQuestionId(TEST_ID, QUESTION_ID))
            .thenReturn(Optional.of(fileAnswer));
        Mockito.when(fileAnswer.getUrl()).thenReturn(URL);

        InputStream inputStream = IOUtils.toInputStream(ESSAY_TEXT, StandardCharsets.UTF_8);
        Mockito.when(resourceStorageService.load(URL)).thenReturn(new InputStreamResource(inputStream));

        Assertions.assertEquals(ESSAY_TEXT, fileAnswerService.downloadEssay(TEST_ID));
    }

    @org.junit.jupiter.api.Test
    void uploadEssayTestNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenThrow(TestNotFoundException.class);

        Assertions.assertThrows(TestNotFoundException.class,
            () -> fileAnswerService.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @org.junit.jupiter.api.Test
    void uploadEssayQuestionNotFound() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY))
            .thenThrow(QuestionNotFoundException.class);

        Assertions.assertThrows(QuestionNotFoundException.class,
            () -> fileAnswerService.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @org.junit.jupiter.api.Test
    void uploadEssaySavingError() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY)).thenReturn(question);
        Mockito.when(resourceStorageService.upload(Mockito.any(), Mockito.eq(Modules.ESSAY), Mockito.eq(TEST_ID)))
            .thenThrow(FileSavingFailedException.class);

        Assertions.assertThrows(FileSavingFailedException.class,
            () -> fileAnswerService.uploadEssay(TEST_ID, ESSAY_TEXT));
    }

    @org.junit.jupiter.api.Test
    void uploadEssayFailTooLong() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY)).thenReturn(question);

        Assertions.assertThrows(TooLongEssayException.class,
            () -> fileAnswerService.uploadEssay(TEST_ID, "1".repeat(513)));
    }

    @org.junit.jupiter.api.Test
    void uploadEssaySuccess() {
        Mockito.when(testsService.getById(TEST_ID)).thenReturn(test);
        Mockito.when(questionService.getQuestionByTestIdAndModule(TEST_ID, Modules.ESSAY)).thenReturn(question);
        Mockito.when(resourceStorageService.upload(Mockito.any(), Mockito.eq(Modules.ESSAY), Mockito.eq(TEST_ID)))
                .thenReturn(URL);

        fileAnswerService.uploadEssay(TEST_ID, ESSAY_TEXT);

        ArgumentCaptor<FileAnswer> captor = ArgumentCaptor.forClass(FileAnswer.class);
        Mockito.verify(fileAnswerRepository).save(captor.capture());

        Assertions.assertEquals(test, captor.getValue().getId().getTest());
        Assertions.assertEquals(question, captor.getValue().getId().getQuestion());
        Assertions.assertEquals(URL, captor.getValue().getUrl());
    }
}
