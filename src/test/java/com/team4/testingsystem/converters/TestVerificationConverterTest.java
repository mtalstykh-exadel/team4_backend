package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.*;
import com.team4.testingsystem.entities.*;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class TestVerificationConverterTest {
    @Mock
    private AnswerService answerService;

    @Mock
    private ErrorReportsService errorReportsService;

    @Mock
    private CoachGradeService gradeService;

    @Mock
    private List<CoachGradeDTO> coachGradeDTOS;

    @Mock
    private CoachAnswerService coachAnswerService;

    @InjectMocks
    private TestVerificationConverter converter;

    private static final Long TEST_ID = 1L;
    private static final Long ESSAY_QUESTION_ID = 2L;
    private static final Long SPEAKING_QUESTION_ID = 3L;
    private static final Long GRAMMAR_QUESTION_ID = 4L;
    private static final String ESSAY_TEXT = "essay";
    private static final String SPEAKING_URL = "url";
    private static final String ERROR_REPORT_BODY = "error here!!!";
    private static final String COACH_ANSWER_BODY = "coach comment";

    private Test test;
    private Level level;
    private Question grammarQuestion;
    private Question essayQuestion;
    private Question speakingQuestion;
    private ErrorReport errorReport;
    private CoachAnswer coachAnswer;

    @BeforeEach
    void init() {
        level = EntityCreatorUtil.createLevel();
        test = EntityCreatorUtil.createTest(EntityCreatorUtil.createUser(), level);
        test.setId(TEST_ID);

        grammarQuestion = EntityCreatorUtil.createQuestion(Modules.GRAMMAR);
        grammarQuestion.setId(GRAMMAR_QUESTION_ID);

        essayQuestion = EntityCreatorUtil.createQuestion(Modules.ESSAY);
        essayQuestion.setId(ESSAY_QUESTION_ID);

        speakingQuestion = EntityCreatorUtil.createQuestion(Modules.SPEAKING);
        speakingQuestion.setId(SPEAKING_QUESTION_ID);

        test.setQuestions(List.of(essayQuestion, speakingQuestion));

        errorReport = new ErrorReport(new TestQuestionID(test, essayQuestion), ERROR_REPORT_BODY);
        coachAnswer = new CoachAnswer(new TestQuestionID(test, grammarQuestion), COACH_ANSWER_BODY);
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoReports() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of());
        Mockito.when(coachAnswerService.getAnswersByTest(TEST_ID)).thenReturn(List.of());
        Mockito.when(answerService.tryDownloadEssay(TEST_ID)).thenReturn(Optional.of(ESSAY_TEXT));
        Mockito.when(answerService.tryDownloadSpeaking(TEST_ID)).thenReturn(Optional.of(SPEAKING_URL));
        Mockito.when(gradeService.getGradesByTest(test.getId()).stream()
                .map(CoachGradeDTO::new)
                .collect(Collectors.toList())).thenReturn(coachGradeDTOS);

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(level.getName(), dto.getTestLevel());
        Assertions.assertTrue(dto.getReportedQuestions().isEmpty());
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoEssay() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(coachAnswerService.getAnswersByTest(TEST_ID)).thenReturn(List.of(coachAnswer));
        Mockito.when(answerService.tryDownloadEssay(TEST_ID)).thenReturn(Optional.empty());
        Mockito.when(answerService.tryDownloadSpeaking(TEST_ID)).thenReturn(Optional.of(SPEAKING_URL));

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(level.getName(), dto.getTestLevel());
        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(new CoachAnswerDTO(coachAnswer), dto.getCoachAnswers().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertNull(dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoSpeaking() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(coachAnswerService.getAnswersByTest(TEST_ID)).thenReturn(List.of(coachAnswer));
        Mockito.when(answerService.tryDownloadEssay(TEST_ID)).thenReturn(Optional.of(ESSAY_TEXT));
        Mockito.when(answerService.tryDownloadSpeaking(TEST_ID)).thenReturn(Optional.empty());

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(level.getName(), dto.getTestLevel());
        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(new CoachAnswerDTO(coachAnswer), dto.getCoachAnswers().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertNull(dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTOFull() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(coachAnswerService.getAnswersByTest(TEST_ID)).thenReturn(List.of(coachAnswer));
        Mockito.when(answerService.tryDownloadEssay(TEST_ID)).thenReturn(Optional.of(ESSAY_TEXT));
        Mockito.when(answerService.tryDownloadSpeaking(TEST_ID)).thenReturn(Optional.of(SPEAKING_URL));

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(level.getName(), dto.getTestLevel());
        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(new CoachAnswerDTO(coachAnswer), dto.getCoachAnswers().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }
}
