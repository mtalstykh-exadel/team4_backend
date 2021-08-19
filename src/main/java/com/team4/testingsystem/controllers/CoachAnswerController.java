package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.CoachAnswerDTO;
import com.team4.testingsystem.services.CoachAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class CoachAnswerController {
    private final CoachAnswerService coachAnswerService;

    @Autowired
    public CoachAnswerController(CoachAnswerService coachAnswerService) {
        this.coachAnswerService = coachAnswerService;
    }

    @PutMapping("/")
    void add(List<CoachAnswerDTO> coachAnswers) {
        coachAnswerService.addAll(coachAnswers);
    }
}
