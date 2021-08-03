package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import com.team4.testingsystem.services.FilesService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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
}
