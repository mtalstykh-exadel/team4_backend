package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.ContentFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentFilesServiceImpl implements ContentFilesService {

    private ContentFilesRepository contentFilesRepository;

    private QuestionRepository questionRepository;

    @Autowired
    public ContentFilesServiceImpl(ContentFilesRepository contentFilesRepository,
                                   QuestionRepository questionRepository) {
        this.contentFilesRepository = contentFilesRepository;
        this.questionRepository = questionRepository;
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
    public void add(String url, Long questionId) {


        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        ContentFile contentFile = new ContentFile(question, url);

        contentFilesRepository.save(contentFile);
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
}