package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AnswerRequest;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.services.AnswerService;
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
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService){
        this.answerService=answerService;
    }

    @ApiOperation(value = "Is used to get an answer from the database by its id")
    @GetMapping(path = "/{id}")
    public Answer get(@PathVariable("id") long id) {
        return answerService.getById(id);
    }

    @ApiOperation(value = "Is used to add an answer")
    @PostMapping(path = "/")
    public void create(@RequestBody AnswerRequest answerRequest) {
        answerService.create(answerRequest);
    }

    @ApiOperation(value = "Is used to change an answer")
    @PutMapping(path = "/{id}")
    public void update(@PathVariable("id") long id, @RequestBody AnswerRequest answerRequest) {
        answerService.update(id, answerRequest);
    }

    @ApiOperation(value = "Is used to remove an answer from the database")
    @DeleteMapping(path = "/{id}")
    public void remove(@PathVariable("id") long id) {
        answerService.removeById(id);
    }
}
