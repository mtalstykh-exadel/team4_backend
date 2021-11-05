package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.Answer;
import com.team4.testingsystem.model.entity.FileAnswer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.FileAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private FileAnswerService fileAnswerService;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private Answer answer;

    @Mock
    private FileAnswer fileAnswer;

    @Mock
    private MultipartFile file;

    private static final Long ANSWER_ID = 1L;
    private static final Long TEST_ID = 2L;
    private static final String ESSAY_TEXT = "text";
    private static final String URL = "some url";

    @Test
    void getByIdNotFound() {
        Mockito.when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(AnswerNotFoundException.class, () -> answerService.getById(1L));
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));
        Assertions.assertEquals(answer, answerService.getById(ANSWER_ID));
    }

    @Test
    void downloadEssay() {
        answerService.downloadEssay(TEST_ID);
        Mockito.verify(fileAnswerService).downloadEssay(TEST_ID);
    }

    @Test
    void tryDownloadEssayNotFound() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenThrow(FileAnswerNotFoundException.class);
        Assertions.assertTrue(answerService.tryDownloadEssay(TEST_ID).isEmpty());
    }

    @Test
    void tryDownloadEssaySuccess() {
        Mockito.when(fileAnswerService.downloadEssay(TEST_ID)).thenReturn(ESSAY_TEXT);
        Assertions.assertEquals(ESSAY_TEXT, answerService.tryDownloadEssay(TEST_ID).orElseThrow());
    }

    @Test
    void uploadEssay() {
        Mockito.when(fileAnswerService.uploadEssay(TEST_ID, ESSAY_TEXT)).thenReturn(fileAnswer);
        Mockito.when(fileAnswer.getUrl()).thenReturn(URL);
        Assertions.assertEquals(URL, answerService.uploadEssay(TEST_ID, ESSAY_TEXT));
   }

    @Test
    void downloadSpeaking() {
        Mockito.when(fileAnswerService.downloadSpeaking(TEST_ID)).thenReturn(URL);
        Assertions.assertEquals(URL, answerService.downloadSpeaking(TEST_ID));
    }

    @Test
    void tryDownloadSpeakingNotFound() {
        Mockito.when(fileAnswerService.downloadSpeaking(TEST_ID)).thenThrow(FileAnswerNotFoundException.class);
        Assertions.assertTrue(answerService.tryDownloadSpeaking(TEST_ID).isEmpty());
    }

    @Test
    void tryDownloadSpeakingSuccess() {
        Mockito.when(fileAnswerService.downloadSpeaking(TEST_ID)).thenReturn(ESSAY_TEXT);
        Assertions.assertEquals(ESSAY_TEXT, answerService.tryDownloadSpeaking(TEST_ID).orElseThrow());
    }

    @Test
    void uploadSpeaking() {
        FileAnswer fileAnswer = new FileAnswer();
        fileAnswer.setUrl(URL);
        Mockito.when(fileAnswerService.uploadSpeaking(file, TEST_ID)).thenReturn(fileAnswer);

        Assertions.assertEquals(URL, answerService.uploadSpeaking(file, TEST_ID));
    }
}
