package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private Question question;

    @Mock
    private ContentFile contentFile;

    @Mock
    private List<ContentFile> contentFiles;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ContentFilesRepository contentFilesRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private final Pageable pageable = PageRequest.of(1, 10);
    private final Modules module = Modules.SPEAKING;
    private final Levels level = Levels.A1;
    private final QuestionStatus status = QuestionStatus.UNARCHIVED;
    private final int page = 1;
    private final int count = 10;
    private static final long ID = 1L;

    @Test
    void getQuestionById() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Question result = questionService.getById(question.getId());
        Assertions.assertEquals(question, result);
    }

    @Test
    void questionByIdNotFoundException() {
        Mockito.when(questionRepository.findById(ID)).thenThrow(new QuestionNotFoundException());
        Assertions.assertThrows(QuestionNotFoundException.class, () -> questionService.getById(ID));
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
        questionService.archiveQuestion(ID);
        verify(questionRepository).archiveQuestion(ID);
    }

    @Test
    void updateQuestion() {
        questionService.updateQuestion(question, ID);
        verify(questionRepository).archiveQuestion(ID);
        verify(questionRepository).save(question);
    }

    @Test
    void getRandomQuestions() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getRandomQuestions(level.name(), module.getName(), pageable)).thenReturn(questions);
        Assertions
                .assertEquals(questions, questionService.getRandomQuestions(level.name(), module.getName(), pageable));
    }

    @Test
    void getRandomQuestionsByContentFile() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getRandomQuestionByContentFile(ID, pageable)).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getRandomQuestionsByContentFile(ID, pageable));
    }

    @Test
    void getQuestionsByTestIdAndModule() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getQuestionsByTestId(ID)).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getQuestionsByTestId(ID));
    }

    @Test
    void archiveQuestionsByContentFileId() {
        Mockito.when(contentFilesRepository.findById(ID))
                .thenReturn(Optional.of(contentFile));
        questionService.archiveQuestionsByContentFileId(ID);
        verify(contentFilesRepository).findById(ID);
    }

    @Test
    void getQuestionsByLevelAndModuleName() {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository.getQuestionsByLevelAndModuleName(level.name(),
                module.getName(), true, pageable))
                .thenReturn(questions);

        Assertions.assertEquals(questions,
                questionService.getQuestionsByLevelAndModuleName(level, module, status, pageable));
    }

    @Test
    void getQuestionByTestIdAndModuleNotFound() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(ID, module.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> questionService.getQuestionByTestIdAndModule(ID, module));
    }

    @Test
    void getQuestionByTestIdAndModuleSuccess() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(ID, module.getName()))
                .thenReturn(Optional.of(question));

        Assertions.assertEquals(question, questionService.getQuestionByTestIdAndModule(ID, module));
    }

    @Test
    void getListening() {
        Mockito.when(contentFilesRepository.findAllByAvailableOrderByIdDesc(true, pageable))
                .thenReturn(contentFiles);
        Assertions.assertEquals(contentFiles, questionService.getListening(null, status, pageable));
    }

    @Test
    void getListeningByLevel() {
        Mockito.when(contentFilesRepository.getContentFiles(level.name(), true, pageable))
                .thenReturn(contentFiles);
        Assertions.assertEquals(contentFiles, questionService.getListening(level, status, pageable));
    }
}
