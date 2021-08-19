package com.team4.testingsystem.utils;

import com.team4.testingsystem.dto.ErrorReportDTO;
import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Answer;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.entities.UserRole;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;

public class EntityCreatorUtil {

    public static final String QUESTION_TEXT = "some text";
    public static final String USERNAME = "name";
    public static final String LOGIN = "login";
    public static final String USER_ROLE = "role";
    public static final String PASSWORD = "password";
    public static final String LANGUAGE = "en";
    public static final String AVATAR = "avatar_url";
    public static final Long ID = 1L;

    public static final Long MODULE_ID = 3L;

    private static Long answerId = 1L;

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

    public static Question createQuestion(Modules module) {
        Question question = createQuestion();
        question.getModule().setName(module.getName());
        return question;
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
        module.setId(MODULE_ID);
        module.setName(Modules.ESSAY.getName());
        return module;
    }


    public static Level createLevel() {
        Level level = new Level();
        level.setId(ID);
        level.setName(Levels.A1.name());
        return level;
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
                .priority(Priority.LOW)
                .level(level)
                .build();
    }

    public static ModuleGradesDTO createModuleGradesDTO() {
        return ModuleGradesDTO.builder()
                .grammar(1)
                .listening(2)
                .essay(3)
                .speaking(4)
                .essayComment("Cool essay")
                .speakingComment("Cool speaking")
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

    public static Answer createAnswer() {
        return Answer.builder()
                .id(7L)
                .answerBody("answer body")
                .question(createQuestion())
                .isCorrect(false)
                .build();
    }

    public static Answer createAnswer(Question question) {
        answerId++;
        return Answer.builder()
                .id(answerId)
                .answerBody("answer body")
                .question(question)
                .isCorrect(false)
                .build();
    }

    public static ChosenOption createChosenOption() {
        Answer answer = createAnswer();
        Question question = answer.getQuestion();
        Test test = createTest(question.getCreator(), question.getLevel());
        test.setId(8L);

        return new ChosenOption(new TestQuestionID(test, question), answer);
    }
}
