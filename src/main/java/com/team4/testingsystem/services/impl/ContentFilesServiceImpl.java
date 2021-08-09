package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.ContentFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentFilesServiceImpl implements ContentFilesService {

    private final ContentFilesRepository contentFilesRepository;

    @Autowired
    public ContentFilesServiceImpl(ContentFilesRepository contentFilesRepository) {
        this.contentFilesRepository = contentFilesRepository;
    }

    @Override
    public Iterable<ContentFile> getAll() {
        return contentFilesRepository.findAll();
    }

    @Override
    public ContentFile getById(long id) {
        return contentFilesRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }

    @Override
    public ContentFile add(String url, List<Question> questions) {
        return contentFilesRepository.save(new ContentFile(url, questions));
    }

    @Override
    public void updateURL(Long id, String newUrl) {
        if (contentFilesRepository.changeUrl(newUrl, id) == 0) {
            throw new FileNotFoundException();
        }
    }

    @Override
    public void removeById(Long id) {
        if (contentFilesRepository.removeById(id) == 0) {
            throw new FileNotFoundException();
        }
    }

    @Override
    public ContentFile getRandomContentFile(String level) {
        return contentFilesRepository.getRandomFiles(level);
    }

    @Override
    public ContentFile getContentFileByQuestionId(Long id) {
        return contentFilesRepository.getContentFileByQuestionId(id);
    }
}