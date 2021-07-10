package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.TestEntity;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TestsService {

    private TestsRepository testsRepository;

    private UsersRepository usersRepository;

    @Autowired
    public TestsService(TestsRepository testsRepository, UsersRepository usersRepository) {
        this.testsRepository = testsRepository;
        this.usersRepository = usersRepository;
    }

    public Iterable<TestEntity> getAllTests() {
        return testsRepository.findAll();
    }

    public Optional<TestEntity> getTestById(long id) {
        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }
            return testsRepository.findById(id);
        }
        catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
        return null;

    }

    public String addNewTest (long userId,
                              String createdAt,
                              String updatedAt,
                              String startedAt,
                              String finishedAt,
                              String status,
                              int evaluation) {

        TestEntity testEntity = new TestEntity();
        try {
            if (!usersRepository.existsById(userId)) {
                throw new NoSuchElementException("User not found");
            }
            testEntity.setUser(usersRepository.findById(userId).get());
            testEntity.setCreatedAt(LocalDateTime.parse(createdAt));
            testEntity.setUpdatedAt(LocalDateTime.parse(updatedAt));
            testEntity.setStartedAt(LocalDateTime.parse(startedAt));
            testEntity.setFinishedAt(LocalDateTime.parse(finishedAt));
            testEntity.setStatus(status);
            testEntity.setEvaluation(evaluation);

            testsRepository.save(testEntity);
            return "Success";

        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return "Fail";
        }

    }


    public String updateTest (long id,
                               long userId,
                               String createdAt,
                               String updatedAt,
                               String startedAt,
                               String finishedAt,
                               String status,
                               int evaluation) {

        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }

            if (!usersRepository.existsById(userId)) {
                throw new NoSuchElementException("User not found");
            }
            TestEntity testEntity = testsRepository.findById(id).get();

            testEntity.setUser(usersRepository.findById(userId).get());
            testEntity.setCreatedAt(LocalDateTime.parse(createdAt));
            testEntity.setUpdatedAt(LocalDateTime.parse(updatedAt));
            testEntity.setStartedAt(LocalDateTime.parse(startedAt));
            testEntity.setFinishedAt(LocalDateTime.parse(finishedAt));
            testEntity.setStatus(status);
            testEntity.setEvaluation(evaluation);

            testsRepository.save(testEntity);

            return "Success";
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return "Fail";
        }
    }

    public String removeTestById(long id) {
        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }

            testsRepository.deleteById(id);
            return "Success";
        }
        catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
        return "Fail";
    }

}