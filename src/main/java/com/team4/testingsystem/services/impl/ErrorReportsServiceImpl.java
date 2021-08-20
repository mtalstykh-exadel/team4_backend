package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.ErrorReportNotFoundException;
import com.team4.testingsystem.repositories.ErrorReportsRepository;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ErrorReportsServiceImpl implements ErrorReportsService {

    private final ErrorReportsRepository errorReportsRepository;

    private final QuestionService questionService;

    private final TestsService testsService;

    private final RestrictionsService restrictionsService;

    @Autowired
    public ErrorReportsServiceImpl(ErrorReportsRepository errorReportsRepository,
                                   QuestionService questionService,
                                   TestsService testsService,
                                   RestrictionsService restrictionsService) {
        this.errorReportsRepository = errorReportsRepository;
        this.questionService = questionService;
        this.testsService = testsService;
        this.restrictionsService = restrictionsService;
    }

    @Override
    public Collection<ErrorReport> getReportsByTest(Long testId) {
        return errorReportsRepository.findAllById_Test(testsService.getById(testId));
    }

    @Override
    public void add(String reportBody, Long questionId, Long testId) {
        Question question = questionService.getById(questionId);

        Test test = testsService.getById(testId);

        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);

        restrictionsService.checkStatus(test, Status.STARTED);

        restrictionsService.checkTestContainsQuestion(test, question);

        TestQuestionID errorReportId = new TestQuestionID(test, question);

        errorReportsRepository.save(new ErrorReport(errorReportId, reportBody));
    }

    @Override
    public void removeByTestAndQuestion(long testId, long questionId) {
        Question question = questionService.getById(questionId);

        Test test = testsService.getById(testId);

        TestQuestionID errorReportId = new TestQuestionID(test, question);

        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);

        restrictionsService.checkStatus(test, Status.STARTED);

        restrictionsService.checkTestContainsQuestion(test, question);

        if (errorReportsRepository.removeById(errorReportId) == 0) {
            throw new ErrorReportNotFoundException();
        }
    }
}
