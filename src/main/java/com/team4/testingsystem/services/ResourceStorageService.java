package com.team4.testingsystem.services;

import com.team4.testingsystem.enums.Modules;
import org.springframework.core.io.Resource;

public interface ResourceStorageService {
    String upload(Resource file, Modules module, Long primaryId);

    void save(String fileUrl, Resource file);

    Resource load(String fileUrl);

    void delete(String fileUrl);
}
