package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Profile("!release&!test")
@Service
public class FileSystemService implements FilesService {
    @Override
    public void save(String fileName, Resource file) throws FileSavingFailedException {
        Path newFilePath = generateFilePath(fileName);

        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(newFilePath.toString()));
        } catch (IOException e) {
            throw new FileSavingFailedException();
        }
    }

    @Override
    public Resource load(String fileName) throws FileLoadingFailedException {
        Path filePath = generateFilePath(fileName);
        if (Files.notExists(filePath)) {
            throw new FileLoadingFailedException();
        }

        return new FileSystemResource(filePath);
    }

    @Override
    public void delete(String fileName) {
        Path filePath = generateFilePath(fileName);
        if (Files.notExists(filePath)) {
            return;
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileDeletingFailedException();
        }
    }

    public Path generateFilePath(String fileName) {
        return Path.of(FileUtils.getTempDirectory() + "/testing-system/" + fileName);
    }
}
