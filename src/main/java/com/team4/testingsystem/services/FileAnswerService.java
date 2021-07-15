package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.FileAnswer;
import com.team4.testingsystem.repositories.FileAnswerRepository;
import com.team4.testingsystem.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

@Service
public class FileAnswerService {
    private final Logger logger = Logger.getLogger(FileAnswerService.class.getName());
    private final FileAnswerRepository fileAnswerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public FileAnswerService(FileAnswerRepository fileAnswerRepository, QuestionRepository questionRepository) {
        this.fileAnswerRepository = fileAnswerRepository;
        this.questionRepository = questionRepository;
    }

    public void create(String url, long questionId) {
        FileAnswer fileAnswer = new FileAnswer();
        try {
            fileAnswer.setUrl(url);
            fileAnswer.setQuestion(questionRepository.findById(questionId).get());
            fileAnswerRepository.save(fileAnswer);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public Optional<FileAnswer> getById(long id) {
        try {
            return fileAnswerRepository.findById(id);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new NoSuchElementException();
        }
    }

    public void update(long id, String url, long questionId) {
        try {
            FileAnswer fileAnswer = fileAnswerRepository.findById(id).get();
            fileAnswer.setUrl(url);
            fileAnswer.setQuestion(questionRepository.findById(questionId).get());
            fileAnswerRepository.save(fileAnswer);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void removeById(long id) {
        try {
            fileAnswerRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
