package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestsServiceImpl implements TestsService {

    private TestsRepository testsRepository;

    private UsersService usersService;


    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository, UsersService usersService) {
        this.testsRepository = testsRepository;
        this.usersService = usersService;
    }

    @Override
    public Iterable<Test> getAll() {
        return testsRepository.findAll();
    }


    @Override
    public Test getById(long id) {
        return testsRepository.findById(id).orElseThrow(TestNotFoundException::new);
    }

    @Override
    public long createForUser(long userId) {


        User user = usersService.getUserById(userId);
        Test test = Test.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(Status.NOT_STARTED)
                .build();
        testsRepository.save(test);

        return test.getId();
    }


    @Override
    public void start(long id) {

        if (testsRepository.start(LocalDateTime.now(), id) == 0) {
            throw new TestNotFoundException();
        }
    }


    @Override
    public void finish(long id, int evaluation) {

        if (testsRepository.finish(LocalDateTime.now(), evaluation, id) == 0) {
            throw new TestNotFoundException();
        }
    }


    @Override
    public void updateEvaluation(long id, int newEvaluation) {

        if (testsRepository.updateEvaluation(LocalDateTime.now(), newEvaluation, id) == 0) {
            throw new TestNotFoundException();
        }

    }

    @Override
    public void removeById(long id) {

        if (testsRepository.removeById(id) == 0) {
            throw new TestNotFoundException();
        }

    }
    @Override
    public void assignCoach(long id, long coachId) {
        User coach = usersService.getUserById(coachId);
        if (testsRepository.assignCoach(coach, id) == 0) {
            throw new TestNotFoundException();
        }

    }

    @Override
    public void deassignCoach(long id) {
        if (testsRepository.deassignCoach(id) == 0) {
            throw new TestNotFoundException();
        }

    }
}