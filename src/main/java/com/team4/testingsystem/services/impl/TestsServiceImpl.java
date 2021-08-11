package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;
    private final TestGeneratingService testGeneratingService;
    private final TestEvaluationService testEvaluationService;

    private final LevelService levelService;
    private final UsersService usersService;

    @Value("${tests-limit:3}")
    private int testsLimit;

    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            TestGeneratingService testGeneratingService,
                            TestEvaluationService testEvaluationService,
                            LevelService levelService,
                            UsersService usersService) {
        this.testsRepository = testsRepository;
        this.testGeneratingService = testGeneratingService;
        this.testEvaluationService = testEvaluationService;
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
    public List<UserTest> getAllUsersAndAssignedTests() {
        Status[] statuses = {Status.ASSIGNED};
        Map<User, Test> assignedTests = getByStatuses(statuses).stream()
                .collect(Collectors.toMap(Test::getUser, Function.identity()));

        return usersService.getAll().stream()
                .map(user -> new UserTest(user, assignedTests.getOrDefault(user, null)))
                .collect(Collectors.toList());
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
                .priority(Priority.LOW)
                .build();

        testsRepository.save(test);
        return test.getId();
    }

    @Override
    public long getTimeLeft(long testId) {
        Test test = getById(testId);
        long timeLeft = test.getStartedAt().plusMinutes(40).toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        if (timeLeft < 0) {
            timeLeft = 0;
        }
        return timeLeft;
    }


    @Override
    public long assignForUser(long userId, Levels levelName, LocalDateTime deadline, Priority priority) {
        Test test = createForUser(userId, levelName)
                .assignedAt(LocalDateTime.now())
                .deadline(deadline)
                .status(Status.ASSIGNED)
                .priority(priority)
                .build();

        testsRepository.save(test);
        return test.getId();
    }

    @Override
    public void deassign(long id) {
        Test test = getById(id);
        if (test.getStartedAt() == null) {
            testsRepository.removeById(id);
        } else {
            testsRepository.deassign(id);
        }
    }

    private Test.Builder createForUser(long userId, Levels levelName) {
        Level level = levelService.getLevelByName(levelName.name());
        User user = usersService.getUserById(userId);
        return Test.builder()
                .user(user)
                .level(level);
    }

    @Override
    public Test start(long id) {

        if (testsRepository.start(LocalDateTime.now(), id) == 0) {
            throw new TestNotFoundException();
        }
        Test test = testGeneratingService.formTest(getById(id));
        save(test);
        setTimer(id);
        return test;
    }

    private void setTimer(long id){
        TimerTask task = new TimerTask() {
            public void run() {
                finish(id);
            }
        };
        
        Timer timer = new Timer(String.valueOf(id));

        long delay = 240000;
        timer.schedule(task, delay);
    }

    @Override
    public void finish(long id) {
        testEvaluationService.countScoreBeforeCoachCheck(getById(id));
        testsRepository.finish(LocalDateTime.now(), id);
    }

    @Override
    public void update(long id) {
        testEvaluationService.updateScoreAfterCoachCheck(getById(id));
        testsRepository.updateEvaluation(LocalDateTime.now(), id);
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
