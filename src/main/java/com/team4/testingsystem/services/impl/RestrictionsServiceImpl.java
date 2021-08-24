package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.exceptions.AnswersAreBadException;
import com.team4.testingsystem.exceptions.AssignmentFailException;
import com.team4.testingsystem.exceptions.CoachAssignmentFailException;
import com.team4.testingsystem.exceptions.FileNotFoundException;
import com.team4.testingsystem.exceptions.IllegalGradeException;
import com.team4.testingsystem.exceptions.NoAudioException;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.exceptions.QuestionOrTopicEditingException;
import com.team4.testingsystem.exceptions.TestAlreadyStartedException;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.services.FilesService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;
import java.util.List;

@Service
public class RestrictionsServiceImpl implements RestrictionsService {

    private final TestsRepository testsRepository;
    private final FilesService filesService;

    @Autowired
    public RestrictionsServiceImpl(TestsRepository testsRepository,
                                   FilesService filesService) {
        this.testsRepository = testsRepository;
        this.filesService = filesService;
    }

    @Override
    public void checkOwnerIsCurrentUser(Test test, Long currentUserId) {
        if (!test.getUser().getId().equals(currentUserId)) {
            throw new AccessControlException("The test has another owner");
        }
    }

    @Override
    public void checkStatus(Test test, Status status) {
        if (!test.getStatus().equals(status)) {
            throw new AccessControlException("The test isn't " + status.name().toLowerCase());

        }
    }

    @Override
    public void checkTestContainsQuestion(Test test, Question question) {
        if (!test.getQuestions().contains(question)) {
            throw new QuestionNotFoundException("The test doesn't contain the question with id = "
                    + question.getId());
        }
    }


    @Override
    public void checkGradeIsCorrect(int grade) {
        if (grade < 0 || grade > 10) {
            throw new IllegalGradeException();
        }
    }

    @Override
    public void checkModuleIsEssayOrSpeaking(Question question) {
        if (!question.getModule().getName().equals(Modules.ESSAY.getName())
                && !question.getModule().getName().equals(Modules.SPEAKING.getName())) {
            throw new AccessControlException("Coach can grade only essay and speaking");
        }
    }

    @Override
    public void checkCoachIsCurrentUser(Test test) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        checkHasAssignedCoach(test);

        if (!test.getCoach().getId().equals(currentUserId)) {
            throw new AccessControlException("The test has another coach");
        }
    }


    @Override
    public void checkIsAssigned(Test test) {
        if (test.getAssignedAt() == null) {
            throw new AssignmentFailException("The test you want to deassign is not assigned");
        }
    }

    @Override
    public void checkHasNoAssignedTests(User user) {
        if (testsRepository.hasAssignedTests(user)) {
            throw new AssignmentFailException("Somebody has already assigned a test for the user");
        }
    }

    @Override
    public void checkNotSelfAssign(User user) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        if (user.getId().equals(currentUserId)) {
            throw new AccessControlException("HR can't assign tests for himself");
        }

    }

    @Override
    public void checkNotSelfDeassign(User user) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        if (user.getId().equals(currentUserId)) {
            throw new AccessControlException("HR can't deassign his own tests");
        }
    }

    @Override
    public void checkHasNoStartedTests(Long userId) {
        if (testsRepository.hasStartedTests(userId)) {
            throw new TestAlreadyStartedException("You can have only one started test at the same time");
        }
    }

    @Override
    public void checkHasAssignedCoach(Test test) {
        if (test.getCoach() == null) {
            throw new CoachAssignmentFailException("The test has no assigned coach");
        }
    }

    @Override
    public void checkHasNoAssignedCoaches(Test test) {
        if (test.getCoach() != null) {
            throw new CoachAssignmentFailException("Somebody has already assigned a coach for the test");
        }
    }

    @Override
    public void checkNotSelfAssignmentCoach(Test test, Long coachId) {
        if (test.getUser().getId().equals(coachId)) {
            throw new CoachAssignmentFailException("Coach can't verify his own test");
        }
    }

    @Override
    public void checkNotVerifiedForCoachDeassign(Test test) {
        if (test.getStatus().equals(Status.VERIFIED)) {
            throw new CoachAssignmentFailException("The coach has already verified the test");
        }
    }

    @Override
    public void checkNotVerified(Test test) {
        if (test.getStatus().equals(Status.VERIFIED)) {
            throw new AccessControlException("The test is already verified");
        }
    }

    @Override
    public void checkNotSelfAssignAdmin(Test test) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        if (test.getUser().getId().equals(currentUserId)) {
            throw new AccessControlException("Admin can assign coaches for his own tests");
        }
    }

    @Override
    public void checkNotSelfDeassignAdmin(Test test) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();

        if (test.getUser().getId().equals(currentUserId)) {

            throw new AccessControlException("Admin can deassign coaches from his own tests");
        }
    }

    @Override
    public void checkModuleIsNotListening(Question question) {
        if (question.getModule().getName().equals(Modules.LISTENING.getName())) {
            throw new AccessControlException("You can't archive one listening question");
        }
    }

    @Override
    public void checkAnswersAreCorrect(List<Answer> answers) {
        if (answers.size() != 4) {
            throw new AnswersAreBadException("Question must have 4 answers");
        }

        if (answers.stream().filter(Answer::isCorrect).count() != 1) {
            throw new AnswersAreBadException("Question must have 1 correct answer");
        }
    }

    @Override
    public void checkNotArchivedQuestion(Question question) {
        if (!question.isAvailable()) {
            throw new QuestionOrTopicEditingException("You can't edit archived questions");
        }
    }

    @Override
    public void checkNotArchivedContentFile(ContentFile contentFile) {
        if (!contentFile.isAvailable()) {
            throw new QuestionOrTopicEditingException("You can't edit archived topics");
        }
    }

    @Override
    public void checkListeningHasAudio(ContentFile contentFile) {
        if (contentFile.getUrl() == null) {
            throw new NoAudioException("You can't add listening topics without audios");
        }
    }

    @Override
    public void checkFileExists(String fileName) {
        if (!filesService.doesFileExist(fileName)) {
            throw new FileNotFoundException("File doesn't exist");
        }
    }
}
