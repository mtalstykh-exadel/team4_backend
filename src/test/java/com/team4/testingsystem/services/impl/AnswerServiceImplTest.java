package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.AnswerNotFoundException;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

    @Mock
    List<Answer> answers;

    @InjectMocks
    QuestionServiceImpl questionService;

    @InjectMocks
    AnswerServiceImpl answerService;

    @Test
    void getByIdSuccess() {
        Mockito.when(answerRepository.findById(answer.getId())).thenReturn(Optional.of(answer));
        Assertions.assertEquals(answer, answerService.getById(answer.getId()));
    }

    @Test
    void getByIdFail() {
        Mockito.when(answerRepository.findById(answer.getId())).thenThrow(new AnswerNotFoundException());
        Assertions.assertThrows(AnswerNotFoundException.class, () -> answerService.getById(answer.getId()));
    }

    @Test
    void getAllByQuestionSuccess() {
        Mockito.when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Mockito.when(question.getAnswers()).thenReturn(answers);
        Assertions.assertEquals(answers, questionService.getQuestionById(question.getId()).getAnswers());
    }
}