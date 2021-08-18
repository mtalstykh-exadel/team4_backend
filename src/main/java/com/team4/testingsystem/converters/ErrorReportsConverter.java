package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorReportsConverter {
    private final TestsService testsService;
    private final QuestionService questionService;

    @Autowired
    public ErrorReportsConverter(TestsService testsService, QuestionService questionService) {
        this.testsService = testsService;
        this.questionService = questionService;
    }

    public ErrorReport convertToEntity(ErrorReportDTO errorReportDTO) {
        Test test = testsService.getById(errorReportDTO.getTestId());
        Question question = questionService.getById(errorReportDTO.getQuestionId());
        TestQuestionID testQuestionID = new TestQuestionID(test, question);
        return new ErrorReport(testQuestionID, errorReportDTO.getReportBody());
    }

}
