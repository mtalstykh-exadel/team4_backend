package com.team4.testingsystem.controllers;

import com.team4.testingsystem.services.FileAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/essay/{testId}")
    public String downloadEssay(@PathVariable Long testId) {
        return null;
    }

    @PostMapping("/essay{testId}")
    public void uploadEssay(@PathVariable Long testId) {
    }
}
