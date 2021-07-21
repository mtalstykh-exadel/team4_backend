package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return testsRepository.findById(id).orElseThrow(TestNotFoundException::new);
    }

    public long createForUser(long userId) {


        User user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Test test = Test.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(Status.NOT_STARTED)
                .build();
        testsRepository.save(test);

        return test.getId();
    }

    public void start(long id) {

        if (testsRepository.start(LocalDateTime.now(), id) == 0) {
            throw new TestNotFoundException();
        }
    }

    public void finish(long id, int evaluation) {

        if (testsRepository.finish(LocalDateTime.now(), evaluation, id) == 0) {
            throw new TestNotFoundException();
        }
    }

    public void updateEvaluation(long id, int newEvaluation) {

        if (testsRepository.updateEvaluation(LocalDateTime.now(), newEvaluation, id) == 0) {
            throw new TestNotFoundException();
        }

    }

    public void removeById(long id) {

        if (testsRepository.removeById(id) == 0) {
            throw new TestNotFoundException();
        }

    }
}