package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ErrorReportsConverter;
import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.services.ErrorReportsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/error_reports")
public class ErrorReportsController {

    private final ErrorReportsService errorReportsService;
    private final ErrorReportsConverter errorReportsConverter;

    @Autowired
    public ErrorReportsController(ErrorReportsService errorReportsService,
                                  ErrorReportsConverter errorReportsConverter) {
        this.errorReportsService = errorReportsService;
        this.errorReportsConverter = errorReportsConverter;
    }

    @ApiOperation(value = "get a list of error reports by test id")
    @GetMapping(path = "/{testId}")
    public List<ErrorReportDTO> getByTestId(@PathVariable long testId) {
        return errorReportsService.getReportsByTest(testId).stream()
               .map(ErrorReportDTO::new)
               .collect(Collectors.toList());
    }

    @ApiOperation(value = "Use it to add or update an error report")
    @PostMapping(path = "/")
    public void add(@RequestBody ErrorReportDTO errorReportDTO) {
        errorReportsService.add(errorReportDTO.getReportBody(),
                errorReportDTO.getQuestionId(),
                errorReportDTO.getTestId());
    }

    @ApiOperation(value = "add a list of error reports")
    @PostMapping(path = "/all")
    public void addAll(@RequestBody List<ErrorReportDTO> errorReports) {
        errorReportsService.addAll(errorReports.stream()
                .map(errorReportsConverter::convertToEntity)
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Use it to remove an error report from the database")
    @DeleteMapping(path = "/")
    public void removeByTestAndQuestion(@RequestParam long testId, @RequestParam long questionId) {
        errorReportsService.removeByTestAndQuestion(testId, questionId);
    }
}
