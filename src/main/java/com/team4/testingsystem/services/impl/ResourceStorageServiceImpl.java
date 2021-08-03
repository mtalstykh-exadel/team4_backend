package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
import com.team4.testingsystem.services.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Profile(value = "!test")
@Service
public class ResourceStorageServiceImpl implements ResourceStorageService {
    private final FilesService filesService;

    @Autowired
    public ResourceStorageServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @Override
    public String upload(Resource file) throws FileSavingFailedException {
        String fileUrl = UUID.randomUUID() + "-" + file.getFilename();
        save(fileUrl, file);
        return fileUrl;
    }

    @Override
    public void save(String fileUrl, Resource file) throws FileSavingFailedException {
        filesService.save(fileUrl, file);
    }

    @Override
    public Resource load(String fileUrl) throws FileLoadingFailedException {
        return filesService.load(fileUrl);
    }

    @Override
    public void delete(String fileUrl) throws FileDeletingFailedException {
        filesService.delete(fileUrl);
    }
}
