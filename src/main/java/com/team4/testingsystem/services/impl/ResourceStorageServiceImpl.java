package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.FilesService;
import com.team4.testingsystem.services.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResourceStorageServiceImpl implements ResourceStorageService {
    private final FilesService filesService;

    @Autowired
    public ResourceStorageServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @Override
    public String upload(Resource file, Modules module, Long primaryId) {
        String randomId = UUID.randomUUID().toString().replace('-', '_');
        String filePath = String.join("/", module.getName(), primaryId.toString(), randomId);

        save(filePath, file);
        return encodeForWeb(filePath);
    }

    @Override
    public void save(String fileUrl, Resource file) {
        filesService.save(fileUrl, file);
    }

    @Override
    public Resource load(String fileUrl) {
        return filesService.load(decodeFromWeb(fileUrl));
    }

    @Override
    public void delete(String fileUrl) {
        filesService.delete(decodeFromWeb(fileUrl));
    }

    private String encodeForWeb(String url) {
        return url.replace('/', '-');
    }

    private String decodeFromWeb(String webUrl) {
        return webUrl.replace('-', '/');
    }
}
