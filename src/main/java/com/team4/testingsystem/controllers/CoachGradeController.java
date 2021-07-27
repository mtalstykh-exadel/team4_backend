package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachGradeDTO;
import com.team4.testingsystem.services.CoachGradeService;
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

    @GetMapping("/{testId}/{questionId}")
    public CoachGradeDTO getGrade(@PathVariable Long testId, @PathVariable Long questionId) {
        return new CoachGradeDTO(gradeService.getGrade(testId, questionId));
    }

    @GetMapping("/{testId}")
    public List<CoachGradeDTO> getGrades(@PathVariable Long testId) {
        return gradeService.getGradesByTest(testId).stream()
                .map(CoachGradeDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/{testId}/{questionId}")
    public void createGrade(@RequestBody CoachGradeDTO coachGradeDto) {
        gradeService.createGrade(coachGradeDto);
    }

    @PutMapping("/{testId}/{questionId}")
    public void updateGrade(@RequestBody CoachGradeDTO coachGradeDto) {
        gradeService.updateGrade(coachGradeDto);
    }
}
