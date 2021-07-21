package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ContentFile;

public interface ContentFilesService {

    Iterable<ContentFile> getAll();

    ContentFile getById(long id);

    void add(String url, Long questionId);

    void updateURL(Long id, String newUrl);

    void removeById(Long id);
}
