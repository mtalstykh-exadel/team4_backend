package com.team4.testingsystem.controllers;

import com.team4.testingsystem.services.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileStorage fileStorage;

    @Autowired
    public FileController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @GetMapping(path = "/{url}")
    public Resource download(@PathVariable("url") String url) {
        return fileStorage.load(url);
    }

    @PostMapping
    public String upload(@RequestParam MultipartFile file) {
        return fileStorage.upload(file.getResource());
    }
}
