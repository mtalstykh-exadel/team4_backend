package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.services.CoachGradeService;
import com.team4.testingsystem.services.TestsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grades")
public class CoachGradeController {
    private final CoachGradeService gradeService;
    private final TestsService testsService;

    @Autowired
    public CoachGradeController(CoachGradeService gradeService, TestsService testsService) {
        this.gradeService = gradeService;
        this.testsService = testsService;
    }

    @ApiOperation(value = "Get all coach's grades for the test")
    @ApiResponse(code = 404, message = "Test not found")
    @GetMapping("/{testId}")
    @Secured("ROLE_COACH")
    public List<CoachGradeDTO> getGrades(@PathVariable Long testId) {
        return gradeService.getGradesByTest(testId).stream()
                .map(CoachGradeDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Is used to submit coach's grades after verification")
    @PutMapping(path = "/submit/{testId}")
    @Secured("ROLE_COACH")
    public void coachSubmit(@PathVariable("testId") long testId) {
        testsService.coachSubmit(testId);
    }

    @ApiOperation(value = "Use it to add or update grading for a single question of the test")
    @PostMapping("/")
    @Secured("ROLE_COACH")
    public void add(@RequestBody CoachGradeDTO gradeDTO) {
        gradeService.add(gradeDTO.getTestId(),
                gradeDTO.getQuestionId(),
                gradeDTO.getGrade(),
                gradeDTO.getComment());
    }
}
