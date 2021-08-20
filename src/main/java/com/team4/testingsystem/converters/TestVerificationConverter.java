package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.*;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.CoachAnswerService;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.ErrorReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestVerificationConverter {
    private final AnswerService answerService;
    private final ErrorReportsService errorReportsService;
    private final CoachGradeService gradeService;
    private final CoachAnswerService coachAnswerService;

    @Autowired
    public TestVerificationConverter(AnswerService answerService,
                                     ErrorReportsService errorReportsService,
                                     CoachGradeService gradeService,
                                     CoachAnswerService coachAnswerService) {
        this.answerService = answerService;
        this.errorReportsService = errorReportsService;
        this.gradeService = gradeService;
        this.coachAnswerService = coachAnswerService;
    }

    public TestVerificationDTO convertToVerificationDTO(Test test) {
        List<ReportedQuestionDTO> reportedQuestions = errorReportsService.getReportsByTest(test.getId()).stream()
                .map(ReportedQuestionDTO::new)
                .collect(Collectors.toList());

        String essayText = answerService.tryDownloadEssay(test.getId()).orElse(null);
        String speakingUrl = answerService.tryDownloadSpeaking(test.getId()).orElse(null);

        List<CoachGradeDTO> grades = gradeService.getGradesByTest(test.getId()).stream()
                .map(CoachGradeDTO::new)
                .collect(Collectors.toList());

        List<CoachAnswerDTO> coachAnswers = coachAnswerService.getAnswersByTest(test.getId())
                .stream()
                .map(CoachAnswerDTO::new)
                .collect(Collectors.toList());

        return TestVerificationDTO.builder()
                .testId(test.getId())
                .testLevel(test.getLevel().getName())
                .reportedQuestions(reportedQuestions)
                .essayQuestion(extractQuestionDTO(test, Modules.ESSAY))
                .essayText(essayText)
                .speakingQuestion(extractQuestionDTO(test, Modules.SPEAKING))
                .speakingUrl(speakingUrl)
                .grades(grades)
                .coachAnswers(coachAnswers)
                .build();
    }

    private QuestionDTO extractQuestionDTO(Test test, Modules module) {
        return test.getQuestions().stream()
                .filter(question -> question.getModule().getName().equals(module.getName()))
                .findFirst()
                .map(QuestionDTO::create)
                .orElseThrow(QuestionNotFoundException::new);
    }
}
