package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ContentFilesServiceImpl implements ContentFilesService {
    private final QuestionService questionService;
    private final ContentFilesRepository contentFilesRepository;
    private final ResourceStorageService storageService;

    @Autowired
    public ContentFilesServiceImpl(QuestionService questionService,
                                   ContentFilesRepository contentFilesRepository,
                                   ResourceStorageService storageService) {
        this.questionService = questionService;
        this.contentFilesRepository = contentFilesRepository;
        this.storageService = storageService;
    }

    @Override
    public Iterable<ContentFile> getAll() {
        return contentFilesRepository.findAll();
    }

    @Override
    public ContentFile getById(long id) {
        return contentFilesRepository.findById(id).orElseThrow(ContentFileNotFoundException::new);
    }

    @Override
    public ContentFile add(MultipartFile file, String topic, List<Question> questions) {
        String url = storageService.upload(file.getResource());
        return contentFilesRepository.save(new ContentFile(url, topic, questions));
    }

    @Transactional
    @Override
    public ContentFile update(MultipartFile file, Long id, String topic, List<Question> questions) {
        if (file == null) {
            questionService.archiveQuestionsByContentFileId(id);
            ContentFile contentFile = contentFilesRepository
                    .findById(id).orElseThrow(ContentFileNotFoundException::new);
            contentFile.getQuestions().addAll(questions);
            return contentFilesRepository.save(contentFile);
        }

        archive(id);
        return add(file, topic, questions);
    }

    @Override
    public void updateURL(Long id, String newUrl) {
        if (contentFilesRepository.changeUrl(newUrl, id) == 0) {
            throw new ContentFileNotFoundException();
        }
    }

    @Override
    public void archive(Long id) {
        if (contentFilesRepository.archiveContentFile(id) == 0) {
            throw new ContentFileNotFoundException();
        }
        questionService.archiveQuestionsByContentFileId(id);
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
