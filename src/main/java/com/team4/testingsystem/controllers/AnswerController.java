package com.team4.testingsystem.controllers;

import com.team4.testingsystem.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/essay/{testId}")
    public String downloadEssay(@PathVariable Long testId) {
        return answerService.downloadEssay(testId);
    }

    @PostMapping("/essay/{testId}")
    public void uploadEssay(@PathVariable Long testId, @RequestBody String text) {
        answerService.uploadEssay(testId, text);
    }
}
