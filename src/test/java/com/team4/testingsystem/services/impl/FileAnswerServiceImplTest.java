package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.Impl.FileAnswerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileAnswerServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private FileAnswerRepository fileAnswerRepository;

    @Mock
    private FileAnswer fileAnswer;

    @Mock
    private Question question;

    @InjectMocks
    private FileAnswerServiceImpl fileAnswerService;

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
    void createSuccess() {
        Mockito.when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> fileAnswerService.create(1L, "", 10L));
    }

    @Test
    void createFail() {
        Mockito.when(questionRepository.findById(10L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.create(1L, "", 10L));
    }

    @Test
    void updateSuccess() {
        Mockito.when(fileAnswerRepository.findById(1L)).thenReturn(Optional.of(fileAnswer));
        Mockito.when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> fileAnswerService.update(1L, "", 10L));
        Mockito.verify(fileAnswerRepository).save(Mockito.any());
    }

    @Test
    void updateNotFoundInFileAnswerRepository() {
        Mockito.when(fileAnswerRepository.findById(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.update(1L, "", 10L));
        Mockito.verify(fileAnswerRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void updateNotFoundInQuestionRepository() {
        Mockito.when(fileAnswerRepository.findById(1L)).thenReturn(Optional.of(fileAnswer));
        Mockito.when(questionRepository.findById(10L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.update(1L, "", 10L));
        Mockito.verify(fileAnswerRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void removeSuccess() {
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> fileAnswerService.removeById(fileAnswer.getId()));
        Mockito.verify(fileAnswerRepository).deleteById(fileAnswer.getId());
    }

    @Test
    void removeFail() {
        Mockito.when(fileAnswerRepository.existsById(fileAnswer.getId())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> fileAnswerService.removeById(fileAnswer.getId()));
    }
}
