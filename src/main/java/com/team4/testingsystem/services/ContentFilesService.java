package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentFilesService {

    Iterable<ContentFile> getAll();

    ContentFile getById(long id);

    ContentFile add(MultipartFile file, String topic, List<Question> questions);

    ContentFile update(MultipartFile file, Long id, String topic, List<Question> questions);

    void updateURL(Long id, String newUrl);

    void removeById(Long id);

    ContentFile getRandomContentFile(String level);

    ContentFile getContentFileByQuestionId(Long id);

    void archiveContentFile(Long id);
}
