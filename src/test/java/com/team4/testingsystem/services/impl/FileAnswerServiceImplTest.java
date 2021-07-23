package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
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
        Mockito.when(fileAnswerRepository.findById(5L)).thenThrow(new FileNotFoundException());
        Assertions.assertThrows(FileNotFoundException.class, () -> fileAnswerService.getById(5L));

    }

    @Test
    void createSuccess() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(1L).build();
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> fileAnswerService.create(fileAnswerRequest));
    }

    @Test
    void createFail() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(1L).build();
        Mockito.when(questionRepository.findById(1L)).thenThrow(QuestionNotFoundException.class);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> fileAnswerService.create(fileAnswerRequest));
    }

    @Test
    void updateSuccess() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(10L).build();
        Mockito.when(fileAnswerRepository.findById(1L)).thenReturn(Optional.of(fileAnswer));
        Mockito.when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> fileAnswerService.update(1L, fileAnswerRequest));
        Mockito.verify(fileAnswerRepository).save(Mockito.any());
    }

    @Test
    void updateNotFoundInFileAnswerRepository() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(10L).build();
        Mockito.when(fileAnswerRepository.findById(1L)).thenThrow(FileNotFoundException.class);
        Assertions.assertThrows(FileNotFoundException.class, () -> fileAnswerService.update(1L, fileAnswerRequest));
        Mockito.verify(fileAnswerRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void updateNotFoundInQuestionRepository() {
        FileAnswerRequest fileAnswerRequest = FileAnswerRequest.builder().url("").questionId(10L).build();
        Mockito.when(fileAnswerRepository.findById(1L)).thenReturn(Optional.of(fileAnswer));
        Mockito.when(questionRepository.findById(10L)).thenThrow(QuestionNotFoundException.class);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> fileAnswerService.update(1L, fileAnswerRequest));
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
        Assertions.assertThrows(FileNotFoundException.class, () -> fileAnswerService.removeById(fileAnswer.getId()));
    }
}
