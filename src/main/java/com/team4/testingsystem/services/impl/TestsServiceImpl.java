package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.Timer;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.TimerRepository;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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

    private final TimerRepository timerRepository;

    @Value("${tests-limit:3}")
    private int testsLimit;


    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            TestGeneratingService testGeneratingService,
                            TestEvaluationService testEvaluationService,
                            LevelService levelService,
                            UsersService usersService,
                            TimerRepository timerRepository) {
        this.testsRepository = testsRepository;
        this.testGeneratingService = testGeneratingService;
        this.testEvaluationService = testEvaluationService;
        this.levelService = levelService;
        this.usersService = usersService;
        this.timerRepository = timerRepository;
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
    public List<Test> getAllUnverifiedTestsByCoach(long coachId) {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        return testsRepository.getAllByAssignedCoachAndStatuses(coachId, statuses);
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
        List<Test> selfStarted = testsRepository
            .getSelfStartedByUserAfter(user, Instant.now().minus(1, ChronoUnit.DAYS));

        if (selfStarted.size() >= testsLimit) {
            throw new TestsLimitExceededException(selfStarted.get(0)
                .getStartedAt().plus(1, ChronoUnit.DAYS).toString());
        }
        Test test = createForUser(userId, levelName)
            .startedAt(Instant.now())
            .status(Status.STARTED)
            .priority(Priority.LOW)
            .build();

        testsRepository.save(test);
        return test.getId();
    }

    @Override
    public long assignForUser(long userId, Levels levelName, Instant deadline, Priority priority) {
        Test test = createForUser(userId, levelName)
            .assignedAt(Instant.now())
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

        if (testsRepository.start(Instant.now(), id) == 0) {
            throw new TestNotFoundException();
        }
        Test test = testGeneratingService.formTest(getById(id));

        test.setFinishTime(Instant.now().plus(40L, ChronoUnit.MINUTES));
        save(test);
        createTimer(test);
        return test;
    }

    private void createTimer(Test test) {
        Timer timer = new Timer(test);
        timerRepository.save(timer);
        startTimer(timer);
    }

    private void startTimer(Timer databaseTimer) {
        Test test = databaseTimer.getTest();
        long testId = test.getId();
        TimerTask task = new TimerTask() {
            public void run() {
                finish(testId, test.getFinishTime());
            }
        };

        java.util.Timer timer = new java.util.Timer(String.valueOf(testId));
        long delay = test.getFinishTime().plus(2L, ChronoUnit.MINUTES).toEpochMilli()
            - Instant.now().toEpochMilli();
        if (delay <= 0) {
            finish(testId, test.getFinishTime());
            timer.cancel();
        } else {
            timer.schedule(task, delay);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startAllTimers() {
        timerRepository.findAll().forEach(this::startTimer);
    }

    @Override
    public void finish(long id, Instant finishDate) {
        Test test = getById(id);
        if (test.getStatus().name().equals(Status.STARTED.name())) {
            timerRepository.deleteByTestId(id);
            testEvaluationService.countScoreBeforeCoachCheck(test);
            testsRepository.finish(finishDate, id);
        }
    }

    @Override
    public void update(long id) {
        testEvaluationService.updateScoreAfterCoachCheck(getById(id));
        testsRepository.updateEvaluation(Instant.now(), id);
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
