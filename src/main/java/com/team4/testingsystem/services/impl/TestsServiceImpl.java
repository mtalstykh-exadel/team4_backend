package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.LevelNotFoundException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.UserNotFoundException;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;
    private final LevelRepository levelRepository;
    private final UsersRepository usersRepository;
    private final TestGeneratingService testGeneratingService;


    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            LevelRepository levelRepository,
                            UsersRepository usersRepository,
                            TestGeneratingService testGeneratingService) {
        this.testsRepository = testsRepository;
        this.levelRepository = levelRepository;
        this.usersRepository = usersRepository;
        this.testGeneratingService = testGeneratingService;
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
    public Test save(Test test) {
        return testsRepository.save(test);
    }

    @Override
    public long createForUser(long userId, Levels levelName) {
        Level level = levelRepository.findByName(levelName.name()).orElseThrow(LevelNotFoundException::new);
        User user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Test test = Test.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(Status.NOT_STARTED)
                .level(level)
                .build();
        testsRepository.save(test);

        return test.getId();
    }


    @Override
    public void start(long id) {

        if (testsRepository.start(LocalDateTime.now(), id) == 0) {
            throw new TestNotFoundException();
        }
        Test test = testGeneratingService.generateTest(getById(id));
        save(test);
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
}