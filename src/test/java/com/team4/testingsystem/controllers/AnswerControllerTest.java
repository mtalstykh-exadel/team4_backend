package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AnswerRequest;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.services.AnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnswerControllerTest {

    @Mock
    private AnswerService answerService;

    @Mock
    private Answer answer;

    @InjectMocks
    private AnswerController answerController;

    @Test
    void getSuccess() {
        Mockito.when(answerService.getById(answer.getId())).thenReturn(answer);
        Assertions.assertEquals(answer, answerController.get(answer.getId()));
    }

    @Test
    void getFail() {
        Mockito.when(answerService.getById(answer.getId())).thenThrow(new FileNotFoundException());
        Assertions.assertThrows(FileNotFoundException.class, () -> answerController.get(answer.getId()));
    }

    @Test
    void create() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(1L).isCorrect(true).build();
        Assertions.assertDoesNotThrow(() -> answerController.create(answerRequest));
        Mockito.verify(answerService).create(answerRequest);
    }

    @Test
    void update() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(1L).isCorrect(true).build();
        Assertions.assertDoesNotThrow(() -> answerController.update(1L, answerRequest));
        Mockito.verify(answerService).update(1L, answerRequest);
    }

    @Test
    void remove() {
        Assertions.assertDoesNotThrow(() -> answerController.remove(1L));
        Mockito.verify(answerService).removeById(1L);
    }
}