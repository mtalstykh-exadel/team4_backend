package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.ReportedQuestionDTO;
import com.team4.testingsystem.dto.TestVerificationDTO;
import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.FileAnswerNotFoundException;
import com.team4.testingsystem.services.AnswerService;
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

@ExtendWith(MockitoExtension.class)
class TestVerificationConverterTest {
    @Mock
    private AnswerService answerService;

    @Mock
    private ErrorReportsService errorReportsService;

    @InjectMocks
    private TestVerificationConverter converter;

    private static final Long TEST_ID = 1L;
    private static final Long ESSAY_QUESTION_ID = 2L;
    private static final Long SPEAKING_QUESTION_ID = 3L;
    private static final String ESSAY_TEXT = "essay";
    private static final String SPEAKING_URL = "url";
    private static final String ERROR_REPORT_BODY = "error here!!!";

    private Test test;
    private Question essayQuestion;
    private Question speakingQuestion;
    private ErrorReport errorReport;

    @BeforeEach
    void init() {
        test = EntityCreatorUtil.createTest(EntityCreatorUtil.createUser(), EntityCreatorUtil.createLevel());
        test.setId(TEST_ID);

        essayQuestion = EntityCreatorUtil.createQuestion(Modules.ESSAY);
        essayQuestion.setId(ESSAY_QUESTION_ID);

        speakingQuestion = EntityCreatorUtil.createQuestion(Modules.SPEAKING);
        speakingQuestion.setId(SPEAKING_QUESTION_ID);

        test.setQuestions(List.of(essayQuestion, speakingQuestion));

        errorReport = new ErrorReport(new TestQuestionID(test, essayQuestion), ERROR_REPORT_BODY);
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoReports() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of());
        Mockito.when(answerService.downloadEssay(TEST_ID)).thenReturn(ESSAY_TEXT);
        Mockito.when(answerService.downloadSpeaking(TEST_ID)).thenReturn(SPEAKING_URL);

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertTrue(dto.getReportedQuestions().isEmpty());
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoEssay() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(answerService.downloadEssay(TEST_ID)).thenThrow(FileAnswerNotFoundException.class);
        Mockito.when(answerService.downloadSpeaking(TEST_ID)).thenReturn(SPEAKING_URL);

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertNull(dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTONoSpeaking() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(answerService.downloadEssay(TEST_ID)).thenReturn(ESSAY_TEXT);
        Mockito.when(answerService.downloadSpeaking(TEST_ID)).thenThrow(FileAnswerNotFoundException.class);

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertNull(dto.getSpeakingUrl());
    }

    @org.junit.jupiter.api.Test
    void convertToVerificationDTOFull() {
        Mockito.when(errorReportsService.getReportsByTest(TEST_ID)).thenReturn(List.of(errorReport));
        Mockito.when(answerService.downloadEssay(TEST_ID)).thenReturn(ESSAY_TEXT);
        Mockito.when(answerService.downloadSpeaking(TEST_ID)).thenReturn(SPEAKING_URL);

        TestVerificationDTO dto = converter.convertToVerificationDTO(test);

        Assertions.assertEquals(1, dto.getReportedQuestions().size());
        Assertions.assertEquals(new ReportedQuestionDTO(errorReport), dto.getReportedQuestions().get(0));
        Assertions.assertEquals(QuestionDTO.create(essayQuestion), dto.getEssayQuestion());
        Assertions.assertEquals(ESSAY_TEXT, dto.getEssayText());
        Assertions.assertEquals(QuestionDTO.create(speakingQuestion), dto.getSpeakingQuestion());
        Assertions.assertEquals(SPEAKING_URL, dto.getSpeakingUrl());
    }
}
