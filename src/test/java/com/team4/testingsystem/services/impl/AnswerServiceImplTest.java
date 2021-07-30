package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerRequest;
import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    Answer answer;

    @Mock
    Question question;

    @Mock
    AnswerRepository answerRepository;

    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    AnswerServiceImpl answerService;

    @Test
    void getByIdSuccess() {
        Mockito.when(answerRepository.findById(answer.getId())).thenReturn(Optional.of(answer));
        Assertions.assertEquals(answer, answerService.getById(answer.getId()));
    }

    @Test
    void getByIdFail() {
        Mockito.when(answerRepository.findById(10L)).thenThrow(new AnswerNotFoundException());
        Assertions.assertThrows(AnswerNotFoundException.class, () -> answerService.getById(10L));
    }

    @Test
    void createSuccess() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(1L).isCorrect(true).build();
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> answerService.create(answerRequest));
    }

    @Test
    void createFail() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(1L).isCorrect(true).build();
        Mockito.when(questionRepository.findById(1L)).thenThrow(QuestionNotFoundException.class);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> answerService.create(answerRequest));
    }

    @Test
    void updateSuccess() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(10L).isCorrect(true).build();
        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        Mockito.when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        Assertions.assertDoesNotThrow(() -> answerService.update(1L, answerRequest));
        Mockito.verify(answerRepository).save(Mockito.any());
    }

    @Test
    void updateNotFoundInAnswerRepository() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(1L).isCorrect(true).build();
        Mockito.when(answerRepository.findById(1L)).thenThrow(FileNotFoundException.class);
        Assertions.assertThrows(FileNotFoundException.class, () -> answerService.update(1L, answerRequest));
        Mockito.verify(answerRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void updateNotFoundInQuestionRepository() {
        AnswerRequest answerRequest = AnswerRequest.builder().answerBody("").questionId(10L).isCorrect(true).build();
        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        Mockito.when(questionRepository.findById(10L)).thenThrow(QuestionNotFoundException.class);
        Assertions.assertThrows(QuestionNotFoundException.class, () -> answerService.update(1L, answerRequest));
        Mockito.verify(answerRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void removeSuccess() {
        Mockito.when(answerRepository.existsById(answer.getId())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> answerService.removeById(answer.getId()));
        Mockito.verify(answerRepository).deleteById(answer.getId());
    }

    @Test
    void removeFail() {
        Mockito.when(answerRepository.existsById(answer.getId())).thenReturn(false);
        Assertions.assertThrows(AnswerNotFoundException.class, () -> answerService.removeById(answer.getId()));
    }
}