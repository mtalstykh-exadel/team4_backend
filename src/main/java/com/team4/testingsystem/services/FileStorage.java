package com.team4.testingsystem.services;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.repositories.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileStorage {
    private final FilesRepository filesRepository;

    @Autowired
    public FileStorage(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public String upload(Resource file) throws FileSavingFailedException {
        String fileUrl = UUID.randomUUID() + "-" + file.getFilename();
        save(fileUrl, file);
        return fileUrl;
    }

    public void save(String fileUrl, Resource file) throws FileSavingFailedException {
        filesRepository.save(fileUrl, file);
    }

    public Resource load(String fileUrl) throws FileLoadingFailedException {
        return filesRepository.load(fileUrl);
    }

    public void delete(String fileUrl) throws FileDeletingFailedException {
        filesRepository.delete(fileUrl);
    }
}
