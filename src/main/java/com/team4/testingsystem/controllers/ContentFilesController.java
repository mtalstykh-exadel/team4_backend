package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.ContentFileRequest;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.services.impl.ContentFilesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/content_files")
public class ContentFilesController {

    @Autowired
    private ContentFilesServiceImpl contentFilesService;

    @GetMapping(path = "/")
    public Iterable<ContentFile> getAll() {
        return contentFilesService.getAll();
    }

    @GetMapping(path = "/{id}")
    public ContentFile getById(@PathVariable("id") long id) {
        return contentFilesService.getById(id);
    }

    @PostMapping(path = "/")
    public void add(@RequestBody ContentFileRequest cfr) {
        contentFilesService.add(cfr.getUrl(), cfr.getQuestionId());
    }

    @PutMapping(path = "/{fileId}")
    public void updateUrl(@PathVariable("fileId") long id, @RequestParam String url) {
        contentFilesService.updateURL(id, url);
    }

    @DeleteMapping(path = "/{id}")
    public void removeById(@PathVariable("id") long id) {
        contentFilesService.removeById(id);
    }
}
