package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
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

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    Question question;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void getQuestionById() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Question result = questionService.getQuestionById(question.getId());
        Assertions.assertEquals(question, result);
    }

    @Test
    void questionByIdNotFoundException() {
        Mockito.when(questionRepository.findById(1L)).thenThrow(new QuestionNotFoundException());
        Assertions.assertThrows(QuestionNotFoundException.class, () -> questionService.getQuestionById(1L));
    }

    @Test
    void createQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Question result = questionService.createQuestion(question);
        Assertions.assertEquals(question, result);
    }

    @Test
    void archiveQuestion(){
        questionService.archiveQuestion(1L);
        verify(questionRepository).archiveQuestion(1L);
    }

    @Test
    void updateQuestion(){
        questionService.updateQuestion(question, 1L);

        verify(questionRepository).archiveQuestion(1L);

        verify(questionRepository).save(question);
    }

}