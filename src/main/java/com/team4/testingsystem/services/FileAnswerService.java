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

    public void create(String url, long question_id) {
        FileAnswer fileAnswer = new FileAnswer();
        try {
            fileAnswer.setUrl(url);
            fileAnswer.setQuestion(questionRepository.findById(question_id).get());
            fileAnswerRepository.save(fileAnswer);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public Iterable<FileAnswer> getAll() {
        return fileAnswerRepository.findAll();
    }

    public Optional<FileAnswer> getById(long id) {
        try {
            return fileAnswerRepository.findById(id);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new NoSuchElementException();
        }
    }

    public void update(long id, String url, long question_id) {
        try {
            FileAnswer fileAnswer = fileAnswerRepository.findById(id).get();
            fileAnswer.setUrl(url);
            fileAnswer.setQuestion(questionRepository.findById(question_id).get());
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
