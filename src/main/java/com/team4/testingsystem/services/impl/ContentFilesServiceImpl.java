package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ContentFileNotFoundException;
import com.team4.testingsystem.repositories.ContentFilesRepository;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.ResourceStorageService;
import com.team4.testingsystem.services.RestrictionsService;
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
    private final RestrictionsService restrictionsService;

    @Autowired
    public ContentFilesServiceImpl(QuestionService questionService,
                                   ContentFilesRepository contentFilesRepository,
                                   ResourceStorageService storageService,
                                   RestrictionsService restrictionsService) {
        this.questionService = questionService;
        this.contentFilesRepository = contentFilesRepository;
        this.storageService = storageService;
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
    public ContentFile add(MultipartFile file, ContentFile contentFile) {
        Long creatorId = JwtTokenUtil.extractUserDetails().getId();
        String url = storageService.upload(file.getResource(), Modules.LISTENING, creatorId);
        contentFile.setUrl(url);
        return contentFilesRepository.save(contentFile);
    }

    @Transactional
    @Override
    public ContentFile update(MultipartFile file, Long id, ContentFile editedContentFile) {
        ContentFile contentFile = getById(id);

        restrictionsService.checkNotArchivedContentFile(contentFile);

        if (file == null) {
            contentFilesRepository.updateAvailable(id, false);
            questionService.archiveQuestionsByContentFileId(id);
            return contentFilesRepository.save(editedContentFile);
        }
        contentFilesRepository.updateAvailable(id, false);
        return add(file, editedContentFile);
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
