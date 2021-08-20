package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.AnswerDTO;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.RestrictionsService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private Question question;

    @Mock
    private ContentFile contentFile;

    @Mock
    private RestrictionsService restrictionsService;

    @Mock
    private List<ContentFile> contentFiles;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ContentFilesRepository contentFilesRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private static final boolean UNAVAILABLE = false;
    private static final Pageable PAGE_REQUEST = PageRequest.of(1, 10);
    private static final Modules SPEAKING = Modules.SPEAKING;
    private static final Levels A1 = Levels.A1;
    private static final QuestionStatus UNARCHIVED = QuestionStatus.UNARCHIVED;
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
        question = EntityCreatorUtil.createQuestion();

        List<Answer> answers = List.of(new Answer());

        question.setAnswers(answers);

        Mockito.when(questionRepository.save(question)).thenReturn(question);
        List<AnswerDTO> textAnswers = new ArrayList<>();
        Question result = questionService.addAnswers(question, textAnswers);

        verify(restrictionsService).checkAnswersAreCorrect(any(List.class));

        Assertions.assertEquals(question.getAnswers(), result.getAnswers());
        Assertions.assertEquals(question, result);
    }

    @Test
    void archiveQuestion() {
        Mockito.when(questionRepository.findById(ID)).thenReturn(Optional.of(question));

        questionService.updateAvailability(ID, UNAVAILABLE);
        verify(restrictionsService).checkModuleIsNotListening(question);

        verify(questionRepository).updateAvailability(ID, UNAVAILABLE);
    }

    @Test
    void updateQuestion() {
        Question question = EntityCreatorUtil.createQuestion();
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Question result = questionService.updateQuestion(question, ID);

        verify(questionRepository).updateAvailability(ID, UNAVAILABLE);
        Assertions.assertEquals(question, result);
    }

    @Test
    void getRandomQuestions() {
        List<Question> questions = List.of(EntityCreatorUtil.createQuestion());
        Mockito.when(questionRepository
                .getRandomQuestions(A1.name(), SPEAKING.getName(), PAGE_REQUEST)).thenReturn(questions);
        Assertions
                .assertEquals(questions, questionService.getRandomQuestions(A1.name(), SPEAKING.getName(), PAGE_REQUEST));
    }

    @Test
    void getRandomQuestionsByContentFile() {
        List<Question> questions = List.of(EntityCreatorUtil.createQuestion());
        Mockito.when(questionRepository
                .getRandomQuestionByContentFile(ID, PAGE_REQUEST)).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getRandomQuestionsByContentFile(ID, PAGE_REQUEST));
    }

    @Test
    void getQuestionsByTestIdAndModule() {
        List<Question> questions = List.of(EntityCreatorUtil.createQuestion());
        Mockito.when(questionRepository
                .getQuestionsByTestId(ID)).thenReturn(questions);
        Assertions.assertEquals(questions, questionService.getQuestionsByTestId(ID));
    }

    @Test
    void archiveQuestionsByContentFileId() {
        ContentFile contentFile = new ContentFile();
        Question question = EntityCreatorUtil.createQuestion();
        contentFile.setQuestions(List.of(question));
        Mockito.when(contentFilesRepository.findById(ID))
                .thenReturn(Optional.of(contentFile));
        questionService.archiveQuestionsByContentFileId(ID);
        verify(contentFilesRepository).findById(ID);
        verify(questionRepository).updateAvailability(question.getId(), UNAVAILABLE);
    }

    @Test
    void getQuestionsByLevelAndModuleName() {
        List<Question> questions = List.of(EntityCreatorUtil.createQuestion());
        Mockito.when(questionRepository.getQuestionsByLevelAndModuleName(A1.name(),
                SPEAKING.getName(), true, PAGE_REQUEST))
                .thenReturn(questions);

        Assertions.assertEquals(questions,
                questionService.getQuestionsByLevelAndModuleName(A1, SPEAKING, UNARCHIVED, PAGE_REQUEST));
    }

    @Test
    void getQuestionByTestIdAndModuleNotFound() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(ID, SPEAKING.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(QuestionNotFoundException.class,
                () -> questionService.getQuestionByTestIdAndModule(ID, SPEAKING));
    }

    @Test
    void getQuestionByTestIdAndModuleSuccess() {
        Mockito.when(questionRepository.getQuestionByTestIdAndModule(ID, SPEAKING.getName()))
                .thenReturn(Optional.of(question));

        Assertions.assertEquals(question, questionService.getQuestionByTestIdAndModule(ID, SPEAKING));
    }

    @Test
    void getListening() {
        Mockito.when(contentFilesRepository.findAllByAvailableOrderByIdDesc(true, PAGE_REQUEST))
                .thenReturn(contentFiles);
        Assertions.assertEquals(contentFiles, questionService.getListening(null, UNARCHIVED, PAGE_REQUEST));
    }

    @Test
    void getListeningByLevel() {
        Mockito.when(contentFilesRepository.getContentFiles(A1.name(), true, PAGE_REQUEST))
                .thenReturn(contentFiles);
        Assertions.assertEquals(contentFiles, questionService.getListening(A1, UNARCHIVED, PAGE_REQUEST));
    }
}
