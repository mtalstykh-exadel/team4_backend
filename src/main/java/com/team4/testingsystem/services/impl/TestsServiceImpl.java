package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;
    private final TestGeneratingService testGeneratingService;
    private final TestConverter testConverter;
    private final LevelService levelService;
    private final UsersService usersService;

    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            TestGeneratingService testGeneratingService,
                            TestConverter testConverter,
                            LevelService levelService,
                            UsersService usersService) {
        this.testsRepository = testsRepository;
        this.testGeneratingService = testGeneratingService;
        this.testConverter = testConverter;
        this.levelService = levelService;
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
    public List<Test> getByUserId(long userId) {
        User user = usersService.getUserById(userId);
        return testsRepository.getAllByUser(user);
    }

    @Override
    public List<Test> getByStatuses(Status[] statuses) {
        return testsRepository.getByStatuses(statuses);
    }

    @Override
    public Test save(Test test) {
        return testsRepository.save(test);
    }

    @Override
    public long startForUser(long userId, Levels levelName) {
        Test test = createForUser(userId, levelName)
                .startedAt(LocalDateTime.now())
                .status(Status.STARTED)
                .build();

        testsRepository.save(test);
        return test.getId();
    }

    @Override
    public long assignForUser(long userId, Levels levelName, LocalDateTime deadline) {
        Test test = createForUser(userId, levelName)
                .assignedAt(LocalDateTime.now())
                .deadline(deadline)
                .status(Status.ASSIGNED)
                .build();

        testsRepository.save(test);
        return test.getId();
    }

    private Test.Builder createForUser(long userId, Levels levelName) {
        Level level = levelService.getLevelByName(levelName.name());
        User user = usersService.getUserById(userId);
        return Test.builder()
                .user(user)
                .level(level);
    }

    @Override
    public TestDTO start(long id) {
        if (testsRepository.start(LocalDateTime.now(), id) == 0) {
            throw new TestNotFoundException();
        }
        Test test = testGeneratingService.formTest(getById(id));
        save(test);
        return testConverter.convertToDTO(test);
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

        if (getById(id).getUser().getId() == coachId) {
            throw new CoachAssignmentFailException();
        }

        testsRepository.assignCoach(coach, id);
    }

    @Override
    public void deassignCoach(long id) {
        if (testsRepository.deassignCoach(id) == 0) {
            throw new TestNotFoundException();
        }
    }
}
