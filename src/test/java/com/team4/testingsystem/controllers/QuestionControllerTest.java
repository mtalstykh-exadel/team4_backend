package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ContentFileConverter;
import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {
    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionConverter questionConverter;

    @Mock
    private ContentFileConverter contentFileConverter;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ContentFile contentFile;

    @Mock
    private ContentFilesService contentFilesService;

    @InjectMocks
    private QuestionController questionController;

    private static final Long ID = 1L;
    private static final Long BAD_ID = 42L;
    private static final boolean UNAVAILABLE = false;
    private static final String TOPIC = "topic";
    private static final String NEW_QUESTION_BODY = "new question body";
    private static final Pageable PAGE_REQUEST = PageRequest.of(1, 10);
    private static final Modules SPEAKING = Modules.SPEAKING;
    private static final Levels A1 = Levels.A1;
    private static final QuestionStatus ARCHIVED = QuestionStatus.ARCHIVED;
    private static final QuestionStatus UNARCHIVED = QuestionStatus.UNARCHIVED;
    private static final int PAGE = 1;
    private static final int COUNT = 10;

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
        Mockito.when(questionService.getQuestionsByLevelAndModuleName(A1, SPEAKING, UNARCHIVED, PAGE_REQUEST))
                .thenReturn(questions);
        List<QuestionDTO> expectedQuestions = questions.stream()
                .map(QuestionDTO::create)
                .collect(Collectors.toList());

        Assertions
                .assertEquals(expectedQuestions, questionController.getQuestions(A1, SPEAKING, UNARCHIVED, PAGE, COUNT));

    }

    @Test
    void getListening() {
        ContentFile contentFile = new ContentFile();
        Question question = EntityCreatorUtil.createQuestion();
        contentFile.setQuestions(List.of(question));
        Mockito.when(contentFilesService.getById(ID)).thenReturn(contentFile);
        Assertions.assertEquals(new ContentFileDTO(contentFile, question.getLevel().getName()),
                questionController.getListening(ID));
    }

    @Test
    void addQuestionWithoutAnswers() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        Mockito.when(questionConverter.convertToEntity(questionDTO)).thenReturn(question);
        Mockito.when(questionService.createQuestion(question)).thenReturn(question);
        QuestionDTO result = questionController.addQuestion(questionDTO);

        Assertions.assertEquals(questionDTO, result);
    }

    @Test
    void addQuestionWithAnswers() {
        Question question = EntityCreatorUtil.createQuestion();
        question.setAnswers(List.of(EntityCreatorUtil.createAnswer()));
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        questionDTO.setAnswers(List.of(AnswerDTO.createWithCorrect(EntityCreatorUtil.createAnswer())));
        Mockito.when(questionConverter.convertToEntity(questionDTO)).thenReturn(question);
        Mockito.when(questionService.createQuestion(question)).thenReturn(question);
        QuestionDTO result = questionController.addQuestion(questionDTO);

        Assertions.assertEquals(questionDTO, result);
    }

    @Test
    void updateAvailability() {
        questionController.updateAvailability(ID, UNAVAILABLE);
        Mockito.verify(questionService).updateAvailability(ID, UNAVAILABLE);
    }

    @Test
    void updateAvailabilityListeningSuccess() {
        questionController.updateAvailabilityListening(ID, UNAVAILABLE);
        Mockito.verify(contentFilesService).updateAvailability(ID, UNAVAILABLE);
    }

    @Test
    void updateAvailabilityListeningFail() {
        doThrow(ContentFileNotFoundException.class).when(contentFilesService).updateAvailability(BAD_ID, UNAVAILABLE);

        Assertions.assertThrows(ContentFileNotFoundException.class,
                () -> questionController.updateAvailabilityListening(BAD_ID, UNAVAILABLE));
    }


    @Test
    void updateQuestionWithoutAnswers() {
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        questionDTO.setQuestionBody(NEW_QUESTION_BODY);
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
    void updateQuestionWithAnswers() {
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        questionDTO.setAnswers(List.of(AnswerDTO.createWithCorrect(EntityCreatorUtil.createAnswer())));
        questionDTO.setQuestionBody(NEW_QUESTION_BODY);
        Question question = EntityCreatorUtil.createQuestion();
        question.setAnswers(List.of(EntityCreatorUtil.createAnswer()));
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
        ContentFileDTO request = new ContentFileDTO();
        request.setId(0L);
        request.setTopic(TOPIC);
        request.setQuestions(List.of());
        Mockito.when(contentFileConverter.convertToEntity(request))
                .thenReturn(contentFile);
        Mockito.when(contentFilesService.add(multipartFile, contentFile))
                .thenReturn(contentFile);
        ContentFileDTO result = questionController.addListening(multipartFile, request);
        result.setTopic(TOPIC);

        Assertions.assertEquals(request, result);
    }

    @Test
    void updateListeningWithFile() {
        ContentFileDTO request = new ContentFileDTO();
        request.setId(0L);
        request.setTopic(TOPIC);
        request.setQuestions(List.of());
        Mockito.when(contentFileConverter.convertToEntity(request))
                .thenReturn(contentFile);
        Mockito.when(contentFilesService.update(multipartFile, ID, contentFile))
                .thenReturn(contentFile);
        ContentFileDTO result = questionController.updateListening(multipartFile, ID, request);
        result.setTopic(TOPIC);

        Assertions.assertEquals(request, result);
    }

    @Test
    void getListeningTopics() {
        questionController.getListeningTopics(null, ARCHIVED, PAGE, COUNT);
        verify(questionService).getListening(null, ARCHIVED, PAGE_REQUEST);
    }

    @Test
    void getListeningTopicsByLevel() {
        questionController.getListeningTopics(A1, UNARCHIVED, PAGE, COUNT);
        verify(questionService).getListening(A1, UNARCHIVED, PAGE_REQUEST);
    }
}
