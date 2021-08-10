package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith( MockitoExtension.class )
class QuestionControllerTest {
    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionConverter questionConverter;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Resource resource;

    @Mock
    private ContentFile contentFile;

    @Mock
    private ResourceStorageService storageService;

    @Mock
    private ContentFilesService contentFilesService;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void getQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        Mockito.when(questionService.getById(question.getId())).thenReturn(question);

        try (MockedStatic<QuestionDTO> mockQuestionDTO = Mockito.mockStatic(QuestionDTO.class)) {
            mockQuestionDTO.when(() -> QuestionDTO.createWithCorrectAnswers(question)).thenReturn(questionDTO);

            QuestionDTO result = questionController.getQuestion(question.getId());

            Assertions.assertEquals(questionDTO, result);
        }
    }

    @Test
    void getQuestionsByLevelAndModuleName() {
        List<Question> questions = Lists.list(EntityCreatorUtil.createQuestion());
        Mockito.when(questionService.getQuestionsByLevelAndModuleName(any(), any())).thenReturn(questions);
        List<QuestionDTO> expectedQuestions = questions.stream()
                .map(QuestionDTO::create)
                .collect(Collectors.toList());
        Assertions.assertEquals(expectedQuestions, questionController.getQuestions(any(), any()));
    }

    @Test
    void addQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        Mockito.when(questionConverter.convertToEntity(questionDTO)).thenReturn(question);
        Mockito.when(questionService.createQuestion(question)).thenReturn(question);
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

        try (MockedStatic<QuestionDTO> mockQuestionDTO = Mockito.mockStatic(QuestionDTO.class)) {
            mockQuestionDTO.when(() -> QuestionDTO.createWithCorrectAnswers(question))
                    .thenReturn(questionDTO);

            QuestionDTO modifiedQuestionDTO = questionController.updateQuestion(questionDTO, question.getId());
            Assertions.assertEquals(questionDTO, modifiedQuestionDTO);
        }
    }

    @Test
    void addListening() {
        Mockito.when(multipartFile.getResource()).thenReturn(resource);
        Mockito.when(contentFilesService.add(any(), any())).thenReturn(contentFile);
        Assertions.assertEquals(new ContentFileDTO(contentFile),
                questionController.addListening(multipartFile, new ArrayList<>()));
    }

    @Test
    void updateListeningWithFile() {
        Mockito.when(contentFilesService.update(any(), any(), any())).thenReturn(contentFile);
        ContentFileDTO result = questionController
                .updateListening(multipartFile, new ArrayList<>(), EntityCreatorUtil.ID);
        Assertions.assertEquals(new ContentFileDTO(contentFile), result);
    }
}
