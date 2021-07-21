package com.team4.testingsystem.services.Impl;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.FileAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileAnswerServiceImpl implements FileAnswerService {
    private final FileAnswerRepository fileAnswerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public FileAnswerServiceImpl(FileAnswerRepository fileAnswerRepository, QuestionRepository questionRepository) {
        this.fileAnswerRepository = fileAnswerRepository;
        this.questionRepository = questionRepository;
    }

    public void create(long id, String url, long questionId) {
        FileAnswer fileAnswer = FileAnswer.builder()
                .id(id)
                .url(url)
                .question(questionRepository.findById(questionId)
                        .orElseThrow(NotFoundException::new))
                .build();
        fileAnswerRepository.save(fileAnswer);
    }

    public FileAnswer getById(long id) {
        return fileAnswerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public void update(long id, String url, long questionId) {
        FileAnswer fileAnswer = FileAnswer.builder()
                .id(fileAnswerRepository.findById(id)
                        .orElseThrow(NotFoundException::new)
                        .getId())
                .url(url)
                .question(questionRepository.findById(questionId)
                        .orElseThrow(NotFoundException::new))
                .build();
        fileAnswerRepository.save(fileAnswer);
    }

    public void removeById(long id) {
        if (!fileAnswerRepository.existsById(id)) {
            throw new NotFoundException();
        }
        fileAnswerRepository.deleteById(id);
    }
}
