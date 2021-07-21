package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.FileAnswer;

public interface FileAnswerService {
    FileAnswer getById(long id);

    void create(long id, String url, long questionId);

    void update(long id, String url, long questionId);

    void removeById(long id);
}
