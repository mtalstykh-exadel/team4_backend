package com.team4.testingsystem.repositories;

import com.team4.testingsystem.exceptions.FileDeletingFailedException;
import com.team4.testingsystem.exceptions.FileLoadingFailedException;
import com.team4.testingsystem.exceptions.FileSavingFailedException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository {
    void save(String fileName, Resource file) throws FileSavingFailedException;

    Resource load(String fileName) throws FileLoadingFailedException;

    void delete(String fileName) throws FileDeletingFailedException;
}
