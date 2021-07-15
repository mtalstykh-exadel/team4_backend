package com.team4.testingsystem.repositories;

import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Profile("!release")
@Repository
public class FileSystemRepository implements FilesRepository {
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

    private Path generateFilePath(String fileName) {
        return Path.of(FileUtils.getTempDirectory() + "/" + UUID.randomUUID() + "-" + fileName);
    }
}
