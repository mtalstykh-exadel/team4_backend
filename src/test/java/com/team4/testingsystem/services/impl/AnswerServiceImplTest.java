package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.FileAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private static final Long ANSWER_ID = 1L;
    private static final Long TEST_ID = 2L;
    private static final String ESSAY_TEXT = "text";

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
    void uploadEssay() {
        answerService.uploadEssay(TEST_ID, ESSAY_TEXT);
        Mockito.verify(fileAnswerService).uploadEssay(TEST_ID, ESSAY_TEXT);
   }
}
