package com.team4.testingsystem.services;

public interface FileAnswerService {
    String getUrl(Long testId, Long questionId);

    void save(Long testId, Long questionId, String url);

    void remove(Long testId, Long questionId);
}
