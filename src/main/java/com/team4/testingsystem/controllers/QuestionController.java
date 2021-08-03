package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.repositories.AnswerRepository;
import com.team4.testingsystem.services.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionConverter questionConverter;
    private final AnswerRepository answerRepository;

    @Autowired
    public QuestionController(QuestionService questionService,
                              QuestionConverter questionConverter,
                              AnswerRepository answerRepository) {
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.answerRepository = answerRepository;
    }

    @ApiOperation(value = "Get a single question from the database by it's id")
    @GetMapping("/{id}")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return questionConverter.convertToDTO(questionService.getById(id));
    }

    @ApiOperation(value = "Add a new question")
    @PostMapping("/")
    public QuestionDTO addQuestion(@RequestBody QuestionDTO questionDTO) {
        Question question = questionService
                .createQuestion(questionConverter.convertToEntity(questionDTO), questionDTO.getAnswers());
        answerRepository.updateCorrect(questionDTO.getCorrect());
        return questionConverter.convertToDTO(question);
    }

    @ApiOperation(value = "Archive the question")
    @DeleteMapping("/{id}")
    public void archiveQuestion(@PathVariable("id") Long id) {
        questionService.archiveQuestion(id);
    }


    @ApiOperation(value = "Change the question")
    @PutMapping("/{id}")
    public QuestionDTO updateQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable("id") Long id) {
        Question resultQuestion = questionService
                .updateQuestion(questionConverter.convertToEntity(questionDTO, id), id);
        return questionConverter.convertToDTO(resultQuestion);
    }
}
