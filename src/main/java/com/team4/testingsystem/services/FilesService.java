package com.team4.testingsystem.services;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import org.springframework.core.io.Resource;

public interface FilesService {
    void save(String fileName, Resource file) throws FileSavingFailedException;

    Resource load(String fileName) throws FileLoadingFailedException;

    void delete(String fileName) throws FileDeletingFailedException;

    boolean isFileExist(String fileName);
}
