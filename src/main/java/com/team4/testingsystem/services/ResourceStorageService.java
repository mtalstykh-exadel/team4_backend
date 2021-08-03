package com.team4.testingsystem.services;

import org.springframework.core.io.Resource;

public interface ResourceStorageService {
    String upload(Resource file);

    void save(String fileUrl, Resource file);

    Resource load(String fileUrl);

    void delete(String fileUrl);
}
