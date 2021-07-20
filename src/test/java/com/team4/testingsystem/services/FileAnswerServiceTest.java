package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileAnswerServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Spy
    private FileAnswer fileAnswer;

    @InjectMocks
    private FileAnswerService fileAnswerService;

    @Test
    void getByIdSuccess() {
        Mockito.when(fileAnswerRepository.findById(fileAnswer.getId())).thenReturn(Optional.of(fileAnswer));
        Assertions.assertEquals(fileAnswer, fileAnswerService.getById(fileAnswer.getId()));
    }

    @Test
    void getByIdFail() {
        Mockito.when(fileAnswerRepository.findById(5L)).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.getById(5L));

    }

    @Test
    void create() {
        fileAnswer.setQuestion(Mockito.mock(Question.class));
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(false);
        fileAnswerService.create(fileAnswer.getId(), fileAnswer.getUrl(), fileAnswer.getQuestion().getId());
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(true);
        Assertions.assertTrue(fileAnswerRepository.existsById(fileAnswer.getId()));
    }

    @Test
    void update() {
        Mockito.when(fileAnswerRepository.findById(fileAnswer.getId())).thenReturn(Optional.of(fileAnswer));
        Question newQuestion = Mockito.mock(Question.class);
        newQuestion.setId(Mockito.anyLong());
        fileAnswer.setUrl(Mockito.anyString());
        fileAnswer.setQuestion(newQuestion);
        fileAnswerService.update(fileAnswer.getId(), fileAnswer.getUrl(), fileAnswer.getQuestion().getId());
        Assertions.assertEquals(fileAnswer, fileAnswerService.getById(fileAnswer.getId()));
    }

    @Test
    void remove() {
        fileAnswerService.removeById(fileAnswer.getId());
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.getById(fileAnswer.getId()));
    }
}
