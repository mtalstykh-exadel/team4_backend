package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TestsService {

    private TestsRepository testsRepository;

    private UsersRepository usersRepository;


    @Autowired
    public TestsService(TestsRepository testsRepository, UsersRepository usersRepository) {
        this.testsRepository = testsRepository;
        this.usersRepository = usersRepository;
    }


    public Iterable<Test> getAll() {
        return testsRepository.findAll();
    }

    public Test getById(long id) {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }
        return testsRepository.findById(id).get();
    }

    public long create(long userId) {

        if (!usersRepository.existsById(userId)){
            throw  new NoSuchElementException("User not found");
        }

        Test test = Test.newBuilder()
                .setUser(usersRepository.findById(userId).get())
                .setCreatedAt(LocalDateTime.now())
                .setStatus("NOT_STARTED")
                .build();
        testsRepository.save(test);
        long id = test.getId();
        return id;
    }

    public void start(long id){
        Test test = getById(id);
        test = test.builder()
                    .setStartedAt(LocalDateTime.now())
                    .setStatus("STARTED")
                    .build();
        testsRepository.save(test);
    }

    public void finish (long id, int evaluation){
        Test test = getById(id);
        test = test.builder()
                .setFinishedAt(LocalDateTime.now())
                .setStatus("FINISHED")
                .setEvaluation(evaluation)
                .build();
        testsRepository.save(test);
    }

    public void updateEvaluation(long id, int newEvaluation) {

        Test test = getById(id);
        test = test.builder()
                .setUpdatedAt(LocalDateTime.now())
                .setEvaluation(newEvaluation)
                .build();

        testsRepository.save(test);
    }

    public void removeById(long id) {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }
            testsRepository.deleteById(id);
    }
}