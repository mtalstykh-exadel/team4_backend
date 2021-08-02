package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.services.ErrorReportsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/error_reports")
public class ErrorReportsController {

    private ErrorReportsService errorReportsService;

    @Autowired
    public ErrorReportsController(ErrorReportsService errorReportsService) {
        this.errorReportsService = errorReportsService;
    }

    @ApiOperation(value = "Use it to get all error reports for the test")
    @GetMapping("/{testId}")
    public List<ErrorReportDTO> getReports(@PathVariable long testId) {
        return errorReportsService.getReportsByTest(testId).stream()
                .map(ErrorReportDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Is used to add an error report")
    @ApiResponse(code = 409, message = "The question is already reported. You can update or delete the report")
    @PostMapping(path = "/")
    public void add(@RequestBody ErrorReportDTO errorReportDTO) {
        errorReportsService.add(errorReportDTO.getReportBody(),
                errorReportDTO.getQuestionId(),
                errorReportDTO.getTestId());
    }

    @ApiOperation(value = "Use it to change report body")
    @PutMapping(path = "/")
    public void updateReportBody(@RequestBody ErrorReportDTO errorReportDTO) {
        errorReportsService.updateReportBody(errorReportDTO.getTestId(),
                errorReportDTO.getQuestionId(),
                errorReportDTO.getReportBody());
    }

    @ApiOperation(value = "Use it to remove an error report from the database")
    @DeleteMapping(path = "/")
    public void removeByTestAndQuestion(@RequestParam long testId, @RequestParam long questionId) {
        errorReportsService.removeByTestAndQuestion(testId, questionId);
    }
}
