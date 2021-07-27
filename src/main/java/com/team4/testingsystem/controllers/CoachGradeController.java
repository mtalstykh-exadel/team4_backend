package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.services.CoachGradeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CoachGradeController(CoachGradeService gradeService) {
        this.gradeService = gradeService;
    }

    @ApiOperation(value = "Get coach's grade for a single question of the test")
    @ApiResponse(code = 404, message = "Test, question or grade not found")
    @GetMapping("/{testId}/{questionId}")
    public CoachGradeDTO getGrade(@PathVariable Long testId, @PathVariable Long questionId) {
        return new CoachGradeDTO(gradeService.getGrade(testId, questionId));
    }

    @ApiOperation(value = "Get all coach's grades for the test")
    @ApiResponse(code = 404, message = "Test not found")
    @GetMapping("/{testId}")
    public List<CoachGradeDTO> getGrades(@PathVariable Long testId) {
        return gradeService.getGradesByTest(testId).stream()
                .map(CoachGradeDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Add grading for a single question of the test")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Test or question not found"),
            @ApiResponse(code = 409, message = "The question is already graded")
    })
    @PostMapping("/")
    public void createGrade(@RequestBody CoachGradeDTO coachGradeDto) {
        gradeService.createGrade(coachGradeDto);
    }

    @ApiOperation(value = "Update grading for a single question of the test")
    @ApiResponse(code = 404, message = "Grade for this question doesn't exist")
    @PutMapping("/")
    public void updateGrade(@RequestBody CoachGradeDTO coachGradeDto) {
        gradeService.updateGrade(coachGradeDto);
    }
}
