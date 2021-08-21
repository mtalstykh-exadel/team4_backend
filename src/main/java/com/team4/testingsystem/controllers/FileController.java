package com.team4.testingsystem.controllers;

import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private final ResourceStorageService resourceStorageService;

    @Autowired
    public FileController(ResourceStorageService resourceStorageService) {
        this.resourceStorageService = resourceStorageService;
    }

    @GetMapping(path = "/{url}")
    public Resource download(@PathVariable("url") String url) {
        return resourceStorageService.load(url);
    }

    @PostMapping(path = "/listening")
    public String uploadListening(@RequestPart MultipartFile file) {
        Long userId = JwtTokenUtil.extractUserDetails().getId();
        return resourceStorageService.upload(file.getResource(), Modules.LISTENING, userId);
    }
}
