package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.ContentFile;

public interface ContentFilesService {

    Iterable<ContentFile> getAll();

    ContentFile getById(long id);

    ContentFile add(ContentFile contentFile);

    ContentFile update(Long id, ContentFile editedContentFile);

    void updateURL(Long id, String newUrl);

    void updateAvailability(Long id, boolean available);

    ContentFile getRandomContentFile(String level);

    ContentFile getContentFileByQuestionId(Long id);
}
