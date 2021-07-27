package com.team4.testingsystem.controllers;

import com.team4.testingsystem.services.CoachGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoachGradeController {
    private final CoachGradeService gradeService;

    @Autowired
    public CoachGradeController(CoachGradeService gradeService) {
        this.gradeService = gradeService;
    }


}
