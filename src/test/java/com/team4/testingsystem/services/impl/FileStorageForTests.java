package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Profile(value = "test")
@Service
public class FileStorageForTests implements FilesService {
    Map<String, Resource> files = new HashMap<>();

    @Override
    public void save(String fileUrl, Resource file) {
        try {
            files.put(fileUrl, file);
        } catch (Exception e) {
            throw new FileSavingFailedException();
        }
    }

    @Override
    public Resource load(String fileUrl) {
        Resource resource = files.get(fileUrl);
        if (!files.containsKey(fileUrl)) {
            throw new FileLoadingFailedException();
        }
        return resource;
    }

    @Override
    public void delete(String fileUrl) {
        if (!files.containsKey(fileUrl)) {
            throw new FileDeletingFailedException();
        }
        files.remove(fileUrl);
    }

    @Override
    public boolean isFileExist(String fileName) {
        Path filePath = generateFilePath(fileName);
        return Files.exists(filePath);

    }

    public Path generateFilePath(String fileName) {
        return Path.of(FileUtils.getTempDirectory() + "/testing-system/" + fileName);
    }
}
