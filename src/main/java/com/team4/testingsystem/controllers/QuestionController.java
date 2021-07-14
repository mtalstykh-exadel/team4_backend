package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionConverter questionConverter;

    @Autowired
    public QuestionController(QuestionService questionService, QuestionConverter questionConverter) {
        this.questionService = questionService;
        this.questionConverter = questionConverter;
    }

    @GetMapping("/{id}")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return questionConverter.convertToDTO(questionService.getQuestionById(id));
    }

    @PostMapping(path = "/add")
    public QuestionDTO addQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionConverter.convertToDTO(
                questionService.createQuestion(questionConverter.convertToEntity(questionDTO))
        );
    }

    @PostMapping("/delete")
    public void archiveQuestion(@RequestParam Long id, @RequestParam boolean isAvailable) {
        questionService.archiveQuestion(isAvailable, id);
    }

    @PostMapping("/update/{id}")
    public QuestionDTO updateQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable("id") Long id) {
        return questionConverter.convertToDTO(
                questionService.updateQuestion(questionConverter.convertToEntity(questionDTO, id), id)
        );
    }
}
