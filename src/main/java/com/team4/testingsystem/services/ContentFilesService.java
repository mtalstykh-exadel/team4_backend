package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ContentFile;
import org.springframework.web.multipart.MultipartFile;

public interface ContentFilesService {

    Iterable<ContentFile> getAll();

    ContentFile getById(long id);

    ContentFile add(MultipartFile file, ContentFile contentFile);

    ContentFile update(MultipartFile file, Long id, ContentFile contentFile);

    void updateURL(Long id, String newUrl);

    void updateAvailability(Long id, boolean available);

    ContentFile getRandomContentFile(String level);

    ContentFile getContentFileByQuestionId(Long id);
}
