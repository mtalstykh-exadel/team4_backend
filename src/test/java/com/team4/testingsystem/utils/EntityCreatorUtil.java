package com.team4.testingsystem.utils;

import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

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
        return QuestionDTO.create(createQuestion());
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

    public static ContentFileDTO createContentFileRequest(Long questionId, String url) {
        ContentFileDTO cfr = new ContentFileDTO();
        cfr.setId(questionId);
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
                .level(level)
                .build();
    }

    public static TestDTO createTestDTO(Test test) {
        List<Question> questions = new ArrayList<>();
        for (Modules module : Modules.values()) {
            Question question = EntityCreatorUtil.createQuestion(createUser());
            Module module1 = new Module();
            module1.setName(module.getName());
            question.setModule(module1);
            questions.add(question);
        }
        Map<String, List<QuestionDTO>> questionsDTO = questions.stream()
                .map(QuestionDTO::create)
                .collect(groupingBy(QuestionDTO::getModule));
        TestDTO testDTO = new TestDTO(test);
        testDTO.setQuestions(questionsDTO);
        return testDTO;
    }

    public static ModuleGradesDTO createModuleGradesDTO(){
        return ModuleGradesDTO.builder()
                .grammar(1)
                .listening(2)
                .essay(3)
                .speaking(4)
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
