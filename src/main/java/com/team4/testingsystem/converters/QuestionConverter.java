package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.impl.LevelServiceImpl;
import com.team4.testingsystem.services.impl.ModuleServiceImpl;
import com.team4.testingsystem.services.impl.QuestionServiceImpl;
import com.team4.testingsystem.services.impl.UsersServiceImpl;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

    private final LevelServiceImpl levelService;
    private final QuestionServiceImpl questionService;
    private final ModuleServiceImpl moduleService;
    private final UsersServiceImpl usersService;

    @Autowired
    public QuestionConverter(LevelServiceImpl levelService, QuestionServiceImpl questionService, ModuleServiceImpl moduleService, UsersServiceImpl usersService) {
        this.levelService = levelService;
        this.questionService = questionService;
        this.moduleService = moduleService;
        this.usersService = usersService;
    }

    public Question convertToEntity(QuestionDTO questionDTO, Long id) {
        Question question = questionService.getQuestionById(id);
        return new Question.Builder()
                .questionBody(getQuestionBody(question, questionDTO))
                .isAvailable(getAvailability(question, questionDTO))
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(getLevel(question, questionDTO))
                .module(getModule(question, questionDTO))
                .build();
    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        return new Question.Builder()
                .questionBody(questionDTO.getQuestionBody())
                .isAvailable(questionDTO.isAvailable())
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(levelService.getLevelByName(questionDTO.getLevel()))
                .module(moduleService.getModuleByName(questionDTO.getModule()))
                .build();
    }

    public QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO.Builder()
                .questionBody(question.getQuestionBody())
                .isAvailable(question.isAvailable())
                .creator(question.getCreator().getName())
                .level(question.getLevel().getName())
                .module(question.getModule().getName())
                .build();
    }

    private String getQuestionBody(Question question, QuestionDTO questionDTO) {
        if (questionDTO.getQuestionBody() == null) {
            return question.getQuestionBody();
        }
        return questionDTO.getQuestionBody();
    }

    private boolean getAvailability(Question question, QuestionDTO questionDTO) {
        if (questionDTO.isAvailable() == null) {
            return question.isAvailable();
        }
        return questionDTO.isAvailable();
    }

    private Level getLevel(Question question, QuestionDTO questionDTO) {
        if (questionDTO.getLevel() == null) {
            return question.getLevel();
        }
        return levelService.getLevelByName(questionDTO.getLevel());
    }

    private Module getModule(Question question, QuestionDTO questionDTO) {
        if (questionDTO.getModule() == null) {
            return question.getModule();
        }
        return moduleService.getModuleByName(questionDTO.getModule());
    }
}
