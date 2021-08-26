package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.ErrorReportAnswer;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.repositories.CoachAnswerRepository;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.ErrorReportAnswerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ErrorReportAnswerServiceImpl implements ErrorReportAnswerService {
    private final CoachAnswerRepository coachAnswerRepository;
    private final ErrorReportsRepository errorReportsRepository;

    @Override
    public List<ErrorReportAnswer> getReportsWithAnswersByTest(Long testId) {
        Map<TestQuestionID, CoachAnswer> answerById = coachAnswerRepository.findAllByTestId(testId)
                .stream()
                .collect(Collectors.toMap(CoachAnswer::getId, Function.identity()));

        return errorReportsRepository.findAllByTestId(testId).stream()
                .map(report -> new ErrorReportAnswer(report, answerById.get(report.getId())))
                .collect(Collectors.toList());
    }
}
