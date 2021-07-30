package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ContentFile;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentFilesService {

    Iterable<ContentFile> getAll();

    ContentFile getById(long id);

    void add(String url, Long questionId);

    void updateURL(Long id, String newUrl);

    void removeById(Long id);

    ContentFile getRandomContentFile(String level);
}
