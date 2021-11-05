package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.model.entity.Timer;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.model.entity.UserTest;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.NotEnoughQuestionsException;
import com.team4.testingsystem.exceptions.TestNotFoundException;
import com.team4.testingsystem.exceptions.TestsLimitExceededException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.NotificationService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestEvaluationService;
import com.team4.testingsystem.services.TestGeneratingService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.services.TimerService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestsServiceImpl implements TestsService {

    private final TestsRepository testsRepository;
    private final TestGeneratingService testGeneratingService;
    private final TestEvaluationService testEvaluationService;
    private final NotificationService notificationService;

    private final LevelService levelService;
    private final UsersService usersService;
    private final RestrictionsService restrictionsService;
    private final TimerService timerService;

    @Value("${tests-limit:3}")
    private int testsLimit;

    @Autowired
    public TestsServiceImpl(TestsRepository testsRepository,
                            TestGeneratingService testGeneratingService,
                            TestEvaluationService testEvaluationService,
                            NotificationService notificationService,
                            LevelService levelService,
                            UsersService usersService,
                            RestrictionsService restrictionsService,
                            TimerService timerService) {
        this.testsRepository = testsRepository;
        this.testGeneratingService = testGeneratingService;
        this.testEvaluationService = testEvaluationService;
        this.notificationService = notificationService;
        this.levelService = levelService;
        this.usersService = usersService;
        this.restrictionsService = restrictionsService;
        this.timerService = timerService;
    }

    @Override
    public Test getById(long id) {
        return testsRepository.findById(id).orElseThrow(TestNotFoundException::new);
    }

    @Override
    public Test getByIdWithRestrictions(long id) {
        Test test = getById(id);
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);
        restrictionsService.checkStatus(test, Status.STARTED);
        return test;
    }

    @Override
    public List<Test> getByUserId(long userId, Levels level, Pageable pageable) {
        if (level != null) {
            return testsRepository.getAllByUserAndLevel(userId, level.name(), pageable);
        }
        return testsRepository.getAllByUserId(userId, pageable);
    }

    @Override
    public List<Test> getByStatuses(Status[] statuses, Pageable pageable) {
        return testsRepository.getByStatuses(statuses, pageable);
    }

    @Override
    public List<Test> getAllUnverifiedTests(Pageable pageable) {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        return testsRepository.getByStatusesExcludingUser(statuses, currentUserId, pageable);
    }

    @Override
    public List<Test> getAllUnverifiedTestsByCoach(long coachId, Pageable pageable) {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        return testsRepository.getAllByAssignedCoachAndStatuses(coachId, statuses, pageable);
    }

    @Override
    public List<UserTest> getAllUsersAndAssignedTests(String nameSubstring, Pageable pageable) {
        if (nameSubstring != null) {
            return usersService.getByNameLike(nameSubstring, pageable).stream()
                    .map(user -> new UserTest(user,
                            testsRepository.getAssignedTestByUserId(user.getId()).orElse(null)))
                    .collect(Collectors.toList());
        }
        return usersService.getAll(pageable).stream()
                .map(user -> new UserTest(user,
                        testsRepository.getAssignedTestByUserId(user.getId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Test> getTestsByUserIdAndLevel(long userId, Levels level, Pageable pageable) {
        if (level != null) {
            return testsRepository.getAllByUserAndLevel(userId, level.name(), pageable);
        }
        return testsRepository.getAllByUserId(userId, pageable);
    }

    @Override
    public Test startTestVerification(long testId) {
        Test test = getById(testId);

        restrictionsService.checkCoachIsCurrentUser(test);
        restrictionsService.checkNotVerified(test);

        if (!test.getStatus().equals(Status.IN_VERIFICATION)) {
            testsRepository.updateStatusByTestId(testId, Status.IN_VERIFICATION);
            test.setStatus(Status.IN_VERIFICATION);
        }

        return test;
    }

    @Override
    public Test save(Test test) {
        return testsRepository.save(test);
    }

    @Override
    public long createNotAssigned(long userId, Levels levelName) {
        User user = usersService.getUserById(userId);
        restrictionsService.checkHasNoStartedTests(userId);
        List<Test> selfStarted = testsRepository
                .getSelfStartedByUserAfter(user, Instant.now().minus(1, ChronoUnit.DAYS));
        if (selfStarted.size() >= testsLimit) {
            throw new TestsLimitExceededException(selfStarted.get(0)
                    .getStartedAt().plus(1, ChronoUnit.DAYS).toString());
        }
        Test test = create(user, levelName)
                .startedAt(Instant.now())
                .status(Status.STARTED)
                .priority(Priority.LOW)
                .listeningAttempts(3)
                .build();
        testsRepository.save(test);
        return test.getId();
    }

    @Override
    public long createAssigned(long userId, Levels levelName, Instant deadline, Priority priority) {
        User user = usersService.getUserById(userId);

        restrictionsService.checkNotSelfAssign(user);
        restrictionsService.checkHasNoAssignedTests(user);
        restrictionsService.checkValidDeadline(deadline);

        Test test = create(user, levelName)
                .assignedAt(Instant.now())
                .deadline(deadline)
                .status(Status.ASSIGNED)
                .priority(priority)
                .listeningAttempts(3)
                .build();
        testsRepository.save(test);

        timerService.createTimer(test, Status.ASSIGNED, deadline);
        notificationService.create(NotificationType.TEST_ASSIGNED, test.getUser(), test);
        return test.getId();
    }

    @Override
    public void deassign(long id) {
        Test test = getById(id);
        restrictionsService.checkIsAssigned(test);
        restrictionsService.checkNotSelfDeassign(test.getUser());
        if (test.getStartedAt() == null) {
            testsRepository.archiveById(id);
        } else {
            testsRepository.deassign(id);
        }

        timerService.deleteTimer(id, Status.ASSIGNED);
        notificationService.create(NotificationType.TEST_DEASSIGNED, test.getUser(), test);
    }

    private Test.Builder create(User user, Levels levelName) {
        Level level = levelService.getLevelByName(levelName.name());
        return Test.builder()
                .user(user)
                .level(level);
    }

    private Test start(Test test) {
        try {
            test = testGeneratingService.formTest(test);
        } catch (NotEnoughQuestionsException e) {
            if (test.getStatus().equals(Status.STARTED)) {
                testsRepository.archiveById(test.getId());
            }
            throw e;
        }

        test.setStatus(Status.STARTED);
        test.setFinishTime(Instant.now().plus(40L, ChronoUnit.MINUTES));
        test.setStartedAt(Instant.now());

        timerService.createTimer(test, Status.STARTED, test.getFinishTime().plus(2L, ChronoUnit.MINUTES));
        notificationService.create(NotificationType.TEST_STARTED, test.getUser(), test);
        return save(test);
    }

    @Override
    public Test startNotAssigned(long testId) {
        Test test = getById(testId);
        return start(test);
    }

    @Override
    public Test startAssigned(long testId) {
        Test test = getById(testId);
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);
        restrictionsService.checkStatus(test, Status.ASSIGNED);
        restrictionsService.checkHasNoStartedTests(currentUserId);
        return start(test);
    }

    @EventListener(Timer.class)
    public void timerExpired(Timer timer) {
        if (!timerService.existsById(timer.getId())) {
            return;
        }

        if (timer.getStatus().equals(Status.STARTED)) {
            finish(timer.getTest().getId(), timer.getTest().getFinishTime());
        } else if (timer.getStatus().equals(Status.ASSIGNED)) {
            testExpired(timer.getTest());
        }
    }

    private void finish(long id, Instant finishDate) {
        Test test = getById(id);
        if (test.getStatus().equals(Status.STARTED)) {
            timerService.deleteTimer(id, Status.STARTED);
            testEvaluationService.countScoreBeforeCoachCheck(test);
            testsRepository.finish(finishDate, id);
        }
    }

    private void testExpired(Test test) {
        testsRepository.updateStatusByTestId(test.getId(), Status.EXPIRED);
        notificationService.create(NotificationType.TEST_EXPIRED, test.getUser(), test);
    }

    @Override
    public void selfFinish(long id) {
        Test test = getById(id);
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);
        restrictionsService.checkStatus(test, Status.STARTED);
        finish(id, Instant.now());
    }

    @Override
    public void coachSubmit(long id) {
        Test test = getById(id);
        restrictionsService.checkCoachIsCurrentUser(test);
        restrictionsService.checkStatus(test, Status.IN_VERIFICATION);
        testEvaluationService.updateScoreAfterCoachCheck(test);
        testsRepository.coachSubmit(Instant.now(), id);
        notificationService.create(NotificationType.TEST_VERIFIED, test.getUser(), test);
    }

    @Override
    public void assignCoach(long id, long coachId) {
        Test test = getById(id);
        restrictionsService.checkHasNoAssignedCoaches(test);
        restrictionsService.checkNotSelfAssignmentCoach(test, coachId);
        restrictionsService.checkStatus(test, Status.COMPLETED);
        restrictionsService.checkNotSelfAssignAdmin(test);
        User coach = usersService.getUserById(coachId);
        testsRepository.assignCoach(coach, id);
        notificationService.create(NotificationType.COACH_ASSIGNED, coach, test);
    }

    @Override
    public void deassignCoach(long id) {
        Test test = getById(id);
        restrictionsService.checkHasAssignedCoach(test);
        restrictionsService.checkNotVerifiedForCoachDeassign(test);
        restrictionsService.checkNotSelfDeassignAdmin(test);
        User coach = test.getCoach();
        testsRepository.deassignCoach(id);
        notificationService.create(NotificationType.COACH_DEASSIGNED, coach, test);
    }

    @Override
    public void spendAttempt(long id) {
        Test test = getById(id);

        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);
        restrictionsService.checkStatus(test, Status.STARTED);

        int attempts = test.getListeningAttempts();
        if (attempts <= 0) {
            throw new AccessControlException("You have no listening attempts left");
        }

        while (testsRepository.updateListeningAttempts(attempts - 1, id) == 0) {
            test = getById(id);

            attempts = test.getListeningAttempts();
            if (attempts <= 0) {
                throw new AccessControlException("You have no listening attempts left");
            }
        }
    }
}
