package com.team4.testingsystem.services.impl;

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
        files.put(fileUrl, file);
    }

    @Override
    public Resource load(String fileUrl) {
        return files.get(fileUrl);
    }

    @Override
    public void delete(String fileUrl) {
        files.remove(fileUrl);
    }
}
