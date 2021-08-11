package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ReportedQuestionDTO;
import com.team4.testingsystem.dto.TestVerificationDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.ErrorReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestVerificationConverter {
    private final AnswerService answerService;
    private final ErrorReportsService errorReportsService;

    @Autowired
    public TestVerificationConverter(AnswerService answerService,
                                     ErrorReportsService errorReportsService) {
        this.answerService = answerService;
        this.errorReportsService = errorReportsService;
    }

    public TestVerificationDTO convertToVerificationDTO(Test test) {
        List<ReportedQuestionDTO> reportedQuestions = errorReportsService.getReportsByTest(test.getId()).stream()
                .map(ReportedQuestionDTO::new)
                .collect(Collectors.toList());

        Question essayQuestion = test.getQuestions().stream()
                .filter(question -> question.getModule().getName().equals(Modules.ESSAY.getName()))
                .findFirst()
                .orElseThrow(QuestionNotFoundException::new);

        Question listeningQuestion = test.getQuestions().stream()
                .filter(question -> question.getModule().getName().equals(Modules.LISTENING.getName()))
                .findFirst()
                .orElseThrow(QuestionNotFoundException::new);

        return TestVerificationDTO.builder()
                .build();
    }
}
