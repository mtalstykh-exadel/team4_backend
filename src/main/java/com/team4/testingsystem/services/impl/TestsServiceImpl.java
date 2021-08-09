package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.UserDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;
    private final TestGeneratingService testGeneratingService;
    private final TestEvaluationService testEvaluationService;
    private final TestConverter testConverter;

    private final LevelService levelService;
    private final UsersService usersService;

    @Value("${tests-limit:3}")
    private int testsLimit;

    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            TestGeneratingService testGeneratingService,
                            TestEvaluationService testEvaluationService,
                            TestConverter testConverter,
                            LevelService levelService,
                            UsersService usersService) {
        this.testsRepository = testsRepository;
        this.testGeneratingService = testGeneratingService;
        this.testEvaluationService = testEvaluationService;
        this.testConverter = testConverter;
        this.levelService = levelService;
        this.usersService = usersService;
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
    public void attachAssignedTests(List<UserDTO> users) {
        Status[] statuses = {Status.ASSIGNED};
        Map<Long, TestInfo> assignedTests = getByStatuses(statuses).stream()
                .collect(Collectors.toMap(test -> test.getUser().getId(), TestInfo::new));

        users.forEach(user -> user.setAssignedTest(assignedTests.getOrDefault(user.getId(), null)));
    }

    @Override
    public Test save(Test test) {
        return testsRepository.save(test);
    }

    @Override
    public long startForUser(long userId, Levels levelName) {
        User user = usersService.getUserById(userId);
        List<Test> selfStarted = testsRepository.getSelfStartedByUserAfter(user, LocalDateTime.now().minusDays(1));

        if (selfStarted.size() >= testsLimit) {
            throw new TestsLimitExceededException(selfStarted.get(0).getStartedAt().plusDays(1).toString());
        }

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
    public void finish(long id) {
        testsRepository.finish(LocalDateTime.now(), testEvaluationService.getEvaluationByTest(getById(id)), id);
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
