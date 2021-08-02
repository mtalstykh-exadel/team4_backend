package com.team4.testingsystem.utils;

import com.team4.testingsystem.dto.ContentFileRequest;
import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.ErrorReport;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Levels;

import java.time.LocalDateTime;

public class EntityCreatorUtil {

    public static final String QUESTION_TEXT = "some text";
    public static final String USERNAME = "name";
    public static final String LOGIN = "login";
    public static final String USER_ROLE = "role";
    public static final String PASSWORD = "password";
    public static final String LANGUAGE = "en";
    public static final Long ID = 1L;

    public static Question createQuestion() {
        return new Question.Builder()
                .id(1L)
                .body(QUESTION_TEXT)
                .module(createModule())
                .level(createLevel())
                .creator(createUser())
                .isAvailable(true)
                .build();
    }

    public static QuestionDTO createQuestionDto() {
        return new QuestionDTO.Builder()
                .body(QUESTION_TEXT)
                .module(Modules.GRAMMAR.getName())
                .level(Levels.A1.name())
                .creator(USERNAME)
                .isAvailable(true)
                .build();
    }

    public static User createUser() {
        User user = new User();
        UserRole userRole = new UserRole();
        userRole.setId(ID.intValue());
        userRole.setRoleName(USER_ROLE);
        user.setId(ID);
        user.setName(USERNAME);
        user.setLanguage(LANGUAGE);
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setRole(userRole);
        return user;
    }

    public static Module createModule() {
        Module module = new Module();
        module.setId(ID);
        module.setName(Modules.GRAMMAR.getName());
        return module;
    }

    public static Level createLevel() {
        Level level = new Level();
        level.setId(ID);
        level.setName(Levels.A1.name());
        return level;
    }

    public static ContentFileRequest createContentFileRequest(Long questionId, String url) {
        ContentFileRequest cfr = new ContentFileRequest();
        cfr.setQuestionId(questionId);
        cfr.setUrl(url);
        return cfr;
    }

    public static ErrorReportDTO createErrorReportDTO(String reportBody, long questionId, long testId) {
        ErrorReportDTO errorReportDTO = ErrorReportDTO
                .builder()
                .questionId(questionId)
                .testId(testId)
                .reportBody(reportBody)
                .build();
        return errorReportDTO;
    }

    public static Test createTest(User user) {
        return Test.builder()
                .user(user)
                .status(Status.STARTED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Question createQuestion(User user) {
        return new Question.Builder()
                .body("some text")
                .module(createModule())
                .level(createLevel())
                .creator(user)
                .isAvailable(true)
                .build();
    }

}
