package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentFilesService {

    private ContentFilesRepository contentFilesRepository;

    private QuestionRepository questionRepository;

    @Autowired
    public ContentFilesService(ContentFilesRepository contentFilesRepository, QuestionRepository questionRepository) {
        this.contentFilesRepository = contentFilesRepository;
        this.questionRepository = questionRepository;
    }


    public Iterable<ContentFile> getAll() {
        return contentFilesRepository.findAll();
    }

    public ContentFile getById(long id) {
        return contentFilesRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }

    public void add(String url, Long questionId) {
        ContentFile contentFile = new ContentFile();

        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        contentFile.setUrl(url);

        contentFile.setQuestion(question);

        contentFilesRepository.save(contentFile);
    }

    public void updateURL(Long id, String newUrl) {
        if (contentFilesRepository.changeUrl(newUrl, id) == 0) {
            throw new FileNotFoundException();
        }
    }


    public void removeById(Long id) {
        if (contentFilesRepository.removeById(id) == 0) {
            throw new FileNotFoundException();
        }
    }

}