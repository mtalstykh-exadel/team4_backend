package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.services.FileAnswerService;
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
@RequestMapping(path = "/file_answer")
public class FileAnswerController {
    private final FileAnswerService fileAnswerService;

    @Autowired
    public FileAnswerController(FileAnswerService fileAnswerService) {
        this.fileAnswerService = fileAnswerService;
    }

    @ApiOperation(value = "Is used to get a file answer from the database by its id")
    @GetMapping(path = "/{id}")
    public FileAnswer get(@PathVariable("id") long id) {
        return fileAnswerService.getById(id);
    }

    @ApiOperation(value = "Is used to add a file answer")
    @PostMapping(path = "/")
    public void create(@RequestBody FileAnswerRequest fileAnswerRequest) {
        fileAnswerService.create(fileAnswerRequest);
    }

    @ApiOperation(value = "Is used to change a file answer")
    @PutMapping(path = "/{id}")
    public void update(@PathVariable("id") long id, @RequestBody FileAnswerRequest fileAnswerRequest) {
        fileAnswerService.update(id, fileAnswerRequest);
    }

    @ApiOperation(value = "Is used to remove a file answer from the database")
    @DeleteMapping(path = "/{id}")
    public void remove(@PathVariable("id") long id) {
        fileAnswerService.removeById(id);
    }
}
