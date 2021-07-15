package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void getQuestionById() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Question result = questionService.getQuestionById(question.getId());
        Assertions.assertEquals(result, question);
    }

    @Test
    void questionByIdNotFoundException() {
        Mockito.when(questionRepository.findById(1L)).thenThrow(new NotFoundException());
        Assertions.assertThrows(NotFoundException.class, () -> questionService.getQuestionById(1L));
    }

    @Test
    void createQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Question result = questionService.createQuestion(question);
        Assertions.assertEquals(result, question);
    }
}