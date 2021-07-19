package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.FileAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileAnswerControllerTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Mock
    private FileAnswerService fileAnswerService;

    @Spy
    private FileAnswer fileAnswer;

    @InjectMocks
    private FileAnswerController fileAnswerController;

    @Test
    void getSuccess() {
        Mockito.when(fileAnswerController.get(Mockito.anyLong())).thenReturn(fileAnswer);
        Assertions.assertEquals(fileAnswer, fileAnswerController.get(Mockito.anyLong()));
    }

    @Test
    void getFail() {
        Mockito.when(fileAnswerController.get(Mockito.anyLong())).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerController.get(Mockito.anyLong()));
    }

    @Test
    void create() {
        fileAnswer.setQuestion(Mockito.mock(Question.class));
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(false);
        fileAnswerController.create(fileAnswer.getId(), fileAnswer.getUrl(), fileAnswer.getQuestion().getId());
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(true);
        Assertions.assertTrue(fileAnswerRepository.existsById(fileAnswer.getId()));
    }

    @Test
    void update() {
        Mockito.when(fileAnswerController.get(fileAnswer.getId())).thenReturn(fileAnswer);
        Question newQuestion = Mockito.mock(Question.class);
        newQuestion.setId(Mockito.anyLong());
        fileAnswer.setUrl(Mockito.anyString());
        fileAnswer.setQuestion(newQuestion);
        fileAnswerController.update(fileAnswer.getId(), fileAnswer.getUrl(), fileAnswer.getQuestion().getId());
        Assertions.assertEquals(fileAnswer, fileAnswerController.get(fileAnswer.getId()));
    }

    @Test
    void remove() {
        fileAnswerController.remove(fileAnswer.getId());
        Mockito.when(fileAnswerController.get(fileAnswer.getId())).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerController.get(fileAnswer.getId()));
    }
}