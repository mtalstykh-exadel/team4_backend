package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.RestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentFilesServiceImpl implements ContentFilesService {
    private final QuestionService questionService;
    private final ContentFilesRepository contentFilesRepository;
    private final RestrictionsService restrictionsService;

    @Autowired
    public ContentFilesServiceImpl(QuestionService questionService,
                                   ContentFilesRepository contentFilesRepository,
                                   RestrictionsService restrictionsService) {
        this.questionService = questionService;
        this.contentFilesRepository = contentFilesRepository;
        this.restrictionsService = restrictionsService;
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
    public ContentFile add(ContentFile contentFile) {
        restrictionsService.checkListeningHasAudio(contentFile);
        restrictionsService.checkFileExists(contentFile.getUrl());

        return contentFilesRepository.save(contentFile);
    }

    @Transactional
    @Override
    public ContentFile update(Long id, ContentFile editedContentFile) {
        ContentFile oldContentFile = getById(id);
        restrictionsService.checkNotArchivedContentFile(editedContentFile);
        restrictionsService.checkFileExists(editedContentFile.getUrl());

        if (editedContentFile.getUrl() == null) {
            editedContentFile.setUrl(oldContentFile.getUrl());
        }

        if (editedContentFile.getUrl().equals(oldContentFile.getUrl())) {
            questionService.archiveQuestionsByContentFileId(id);
            editedContentFile.setId(id);
            return contentFilesRepository.save(editedContentFile);
        }

        contentFilesRepository.updateAvailable(id, false);
        editedContentFile.setId(null);
        return add(editedContentFile);
    }

    @Override
    public void updateURL(Long id, String newUrl) {
        if (contentFilesRepository.changeUrl(newUrl, id) == 0) {
            throw new ContentFileNotFoundException();
        }
    }

    @Override
    public void updateAvailability(Long id, boolean available) {
        if (contentFilesRepository.updateAvailable(id, available) == 0) {
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
