package com.team4.testingsystem.controllers;

import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.FileAnswerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation("Upload an answer file for the speaking module")
    @PostMapping("/speaking/{testId}")
    public String uploadSpeaking(@RequestPart MultipartFile file,
                                 @PathVariable("testId") Long testId) {
        return fileAnswerService.addFileAnswer(file, testId, Modules.SPEAKING).getUrl();
    }

    @ApiOperation("Get an answer file for the speaking module")
    @GetMapping("/speaking/{testId}")
    public String downloadSpeaking(@PathVariable("testId") Long testId) {
        return fileAnswerService.getSpeaking(testId);
    }
}
