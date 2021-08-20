package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ContentFile add(MultipartFile file, ContentFile contentFile) {
        Long creatorId = JwtTokenUtil.extractUserDetails().getId();
        String url = storageService.upload(file.getResource(), Modules.LISTENING, creatorId);
        contentFile.setUrl(url);
        return contentFilesRepository.save(contentFile);
    }

    @Transactional
    @Override
    public ContentFile update(MultipartFile file, Long id, ContentFile contentFile) {
        if (file == null) {
            questionService.archiveQuestionsByContentFileId(id);
            return contentFilesRepository.save(contentFile);
        }
        updateAvailability(id, false);
        return add(file, contentFile);
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
