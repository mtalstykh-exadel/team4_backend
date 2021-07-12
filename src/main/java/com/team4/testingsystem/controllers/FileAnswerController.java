package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.services.FileAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/file_answer")
public class FileAnswerController {
    private final FileAnswerService fileAnswerService;

    @Autowired
    public FileAnswerController(FileAnswerService fileAnswerService) {
        this.fileAnswerService = fileAnswerService;
    }

    @GetMapping(path = "/get")
    @ResponseBody
    public Optional<FileAnswer> getFileAnswer(@RequestParam long id) {
        return fileAnswerService.getById(id);
    }

    @GetMapping(path = "/all")
    public Iterable<FileAnswer> getAllFileAnswers() {
        return fileAnswerService.getAll();
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public void createFileAnswer(@RequestParam String url, @RequestParam long question_id) {
        fileAnswerService.create(url, question_id);
    }

    @PostMapping(path = "/update")
    @ResponseBody
    public void updateFileAnswer(@RequestParam long id, @RequestParam String url, @RequestParam long question_id) {
        fileAnswerService.update(id, url, question_id);
    }

    @DeleteMapping(path = "/remove")
    @ResponseBody
    public void removeFileAnswer(@RequestParam long id) {
        fileAnswerService.removeById(id);
    }
}
