package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {
    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionConverter questionConverter;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void getQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        Mockito.when(questionService.getQuestionById(question.getId())).thenReturn(question);
        Mockito.when(questionConverter.convertToDTO(question)).thenReturn(questionDTO);
        QuestionDTO result = questionController.getQuestion(question.getId());

        Assertions.assertEquals(questionDTO, result);
    }


    @Test
    void addQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        Mockito.when(questionConverter.convertToEntity(questionDTO)).thenReturn(question);
        Mockito.when(questionService.createQuestion(question)).thenReturn(question);
        Mockito.when(questionConverter.convertToDTO(question)).thenReturn(questionDTO);
        QuestionDTO result = questionController.addQuestion(questionDTO);

        Assertions.assertEquals(questionDTO, result);
    }

    @Test
    void archiveQuestion() {
        questionController.archiveQuestion(1L);
        Mockito.verify(questionService).archiveQuestion(1L);
    }

    @Test
    void updateQuestion() {
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        questionDTO.setQuestionBody("new question body");
        Question question = EntityCreatorUtil.createQuestion();
        question.setBody(questionDTO.getQuestionBody());
        Mockito.when(questionConverter.convertToEntity(questionDTO, question.getId())).thenReturn(question);
        Mockito.when(questionService.updateQuestion(question, question.getId())).thenReturn(question);
        Mockito.when(questionConverter.convertToDTO(question)).thenReturn(questionDTO);

        QuestionDTO modifiedQuestionDTO = questionController.updateQuestion(questionDTO, question.getId());
        Assertions.assertEquals(questionDTO, modifiedQuestionDTO);
    }
}