package com.team4.testingsystem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @GetMapping("/essay")
    public String downloadEssay(@RequestParam Long testId) {
        return "";
    }

    @PostMapping("/essay")
    public void uploadEssay(@RequestParam Long testId, @RequestBody String text) {
    }
}
