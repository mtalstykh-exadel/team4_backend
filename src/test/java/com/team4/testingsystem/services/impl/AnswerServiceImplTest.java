package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
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

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private Answer answer;

    @Test
    void getByIdNotFound() {
        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AnswerNotFoundException.class, () -> answerService.getById(1L));
    }

    @Test
    void getByIdSuccess() {
        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        Assertions.assertEquals(answer, answerService.getById(1L));
    }
}
