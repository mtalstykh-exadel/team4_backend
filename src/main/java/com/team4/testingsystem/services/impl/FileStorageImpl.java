package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.repositories.FilesRepository;
import com.team4.testingsystem.services.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Profile(value = "!test")
@Service
public class FileStorageImpl implements FileStorage {
    private final FilesRepository filesRepository;

    @Autowired
    public FileStorageImpl(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Override
    public String upload(Resource file) throws FileSavingFailedException {
        String fileUrl = UUID.randomUUID() + "-" + file.getFilename();
        save(fileUrl, file);
        return fileUrl;
    }

    @Override
    public void save(String fileUrl, Resource file) throws FileSavingFailedException {
        filesRepository.save(fileUrl, file);
    }

    @Override
    public Resource load(String fileUrl) throws FileLoadingFailedException {
        return filesRepository.load(fileUrl);
    }

    @Override
    public void delete(String fileUrl) throws FileDeletingFailedException {
        filesRepository.delete(fileUrl);
    }
}
