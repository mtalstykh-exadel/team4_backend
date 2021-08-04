package com.team4.testingsystem.utils;

import com.team4.testingsystem.dto.ContentFileRequest;
import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.dto.QuestionDTO;
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
    public static final String AVATAR = "avatar_url";
    public static final Long ID = 1L;

    public static Question createQuestion() {
        return Question.builder()
                .id(1L)
                .body(QUESTION_TEXT)
                .module(createModule())
                .level(createLevel())
                .creator(createUser())
                .isAvailable(true)
                .build();
    }

    public static QuestionDTO createQuestionDto() {
        return new QuestionDTO(createQuestion());
    }

    public static User createUser() {
        UserRole userRole = new UserRole();
        userRole.setId(ID.intValue());
        userRole.setRoleName(USER_ROLE);
        return User.builder()
                .id(ID)
                .name(USERNAME)
                .language(LANGUAGE)
                .login(LOGIN)
                .password(PASSWORD)
                .role(userRole)
                .avatar(AVATAR)
                .build();
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
        return ErrorReportDTO.builder()
                .questionId(questionId)
                .testId(testId)
                .reportBody(reportBody)
                .build();
    }

    public static Test createTest(User user, Level level) {
        return Test.builder()
                .user(user)
                .status(Status.STARTED)
                .createdAt(LocalDateTime.now())
                .level(level)
                .build();
    }

    public static Question createQuestion(User user) {
        return Question.builder()
                .body("some text")
                .module(createModule())
                .level(createLevel())
                .creator(user)
                .isAvailable(true)
                .build();
    }
}
