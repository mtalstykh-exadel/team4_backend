package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.impl.FileAnswerServiceImpl;
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
    private QuestionRepository questionRepository;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Mock
    private FileAnswerServiceImpl fileAnswerService;

    @Mock
    private FileAnswer fileAnswer;

    @InjectMocks
    private FileAnswerController fileAnswerController;

    @Test
    void getSuccess() {
        Mockito.when(fileAnswerService.getById(fileAnswer.getId())).thenReturn(fileAnswer);
        Assertions.assertEquals(fileAnswer, fileAnswerController.get(fileAnswer.getId()));
    }

    @Test
    void getFail() {
        Mockito.when(fileAnswerService.getById(fileAnswer.getId())).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerController.get(fileAnswer.getId()));
    }

    @Test
    void create() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(1L).build();
        Assertions.assertDoesNotThrow(() -> fileAnswerController.create(fileAnswerRequest));
        Mockito.verify(fileAnswerService).create(fileAnswerRequest);
    }

    @Test
    void update() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(1L).build();
        Assertions.assertDoesNotThrow(() -> fileAnswerController.update(1L, fileAnswerRequest));
        Mockito.verify(fileAnswerService).update(1L, fileAnswerRequest);
    }

    @Test
    void remove() {
        Assertions.assertDoesNotThrow(() -> fileAnswerController.remove(1L));
        Mockito.verify(fileAnswerService).removeById(1L);
    }
}