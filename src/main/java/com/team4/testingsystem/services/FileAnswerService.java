package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.FileAnswerRequest;
import com.team4.testingsystem.entities.FileAnswer;

public interface FileAnswerService {
    FileAnswer getById(long id);

    void create(FileAnswerRequest fileAnswerRequest);

    void update(long id, FileAnswerRequest fileAnswerRequest);

    void removeById(long id);
}
