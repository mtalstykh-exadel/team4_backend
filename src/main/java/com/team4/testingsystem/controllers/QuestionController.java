package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable("id") Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping("/update")
    public Question updateQuestion(@RequestParam Long id, boolean isAvailable) {
        return questionService.updateAvailability(id, isAvailable);
    }

}
