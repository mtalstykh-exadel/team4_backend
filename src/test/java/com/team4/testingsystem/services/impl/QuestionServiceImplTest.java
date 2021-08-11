package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
        Question result = questionService.getById(question.getId());
        Assertions.assertEquals(question, result);
    }

    @Test
    void questionByIdNotFoundException() {
        Mockito.when(questionRepository.findById(1L)).thenThrow(new QuestionNotFoundException());
        Assertions.assertThrows(QuestionNotFoundException.class, () -> questionService.getById(1L));
    }

    @Test
    void createQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Question result = questionService.createQuestion(question);
        Assertions.assertEquals(question, result);
    }

    @Test
    void addAnswers() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        List<AnswerDTO> textAnswers = new ArrayList<>();
        Question result = questionService.addAnswers(question, textAnswers);
        Assertions.assertEquals(question, result);
    }

    @Test
    void archiveQuestion() {
        questionService.archiveQuestion(1L);
        verify(questionRepository).archiveQuestion(1L);
    }

    @Test
    void updateQuestion() {
        questionService.updateQuestion(question, 1L);
        verify(questionRepository).archiveQuestion(1L);
        verify(questionRepository).save(question);
    }

    @Test
    void getRandomQuestions() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getRandomQuestions(any(), any(), any())).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getRandomQuestions(any(), any(), any()));
    }

    @Test
    void getRandomQuestionsByContentFile() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getRandomQuestionByContentFile(any(), any())).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getRandomQuestionsByContentFile(any(), any()));
    }

    @Test
    void getQuestionsByTestIdAndModule() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getQuestionsByTestId(any())).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getQuestionsByTestId(any()));
    }

    @Test
    void getQuestionsByLevelAndModuleName() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository.getQuestionsByLevelAndModuleName(Levels.A1.name(), Modules.ESSAY.getName()))
                .thenReturn(questions);
        Assertions.assertEquals(questions,
                questionService.getQuestionsByLevelAndModuleName(Levels.A1, Modules.ESSAY));
    }

    @Test
    void getQuestionByTestIdAndModuleNotFound() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(1L, Modules.ESSAY.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> questionService.getQuestionByTestIdAndModule(1L, Modules.ESSAY));
    }

    @Test
    void getQuestionByTestIdAndModuleSuccess() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(1L, Modules.ESSAY.getName()))
                .thenReturn(Optional.of(question));

        Assertions.assertEquals(question, questionService.getQuestionByTestIdAndModule(1L, Modules.ESSAY));
    }
}
