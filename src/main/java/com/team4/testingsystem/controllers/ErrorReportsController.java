package com.team4.testingsystem.controllers;

import com.team4.testingsystem.model.dto.ErrorReportDTO;
import com.team4.testingsystem.services.ErrorReportsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/error_reports")
public class ErrorReportsController {

    private final ErrorReportsService errorReportsService;

    @Autowired
    public ErrorReportsController(ErrorReportsService errorReportsService) {
        this.errorReportsService = errorReportsService;
    }

    @ApiOperation(value = "Use it to add or update an error report")
    @PostMapping(path = "/")
    public void add(@RequestBody ErrorReportDTO errorReportDTO) {
        errorReportsService.add(errorReportDTO.getReportBody(),
                errorReportDTO.getQuestionId(),
                errorReportDTO.getTestId());
    }

    @ApiOperation(value = "Use it to remove an error report from the database")
    @DeleteMapping(path = "/")
    public void removeByTestAndQuestion(@RequestParam long testId, @RequestParam long questionId) {
        errorReportsService.removeByTestAndQuestion(testId, questionId);
    }
}
