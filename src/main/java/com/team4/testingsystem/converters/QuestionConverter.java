package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.ModuleService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

    private final LevelService levelService;
    private final QuestionService questionService;
    private final ModuleService moduleService;
    private final UsersService usersService;

    @Autowired
    public QuestionConverter(LevelService levelService,
                             QuestionService questionService,
                             ModuleService moduleService,
                             UsersService usersService) {
        this.levelService = levelService;
        this.questionService = questionService;
        this.moduleService = moduleService;
        this.usersService = usersService;
    }

    public Question convertToEntity(QuestionDTO questionDTO, Long id) {
        Question question = questionService.getById(id);
        return Question.builder()
                .body(getQuestionBody(question, questionDTO))
                .isAvailable(getAvailability(question, questionDTO))
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(getLevel(question, questionDTO))
                .module(getModule(question, questionDTO))
                .build();
    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        return Question.builder()
                .body(questionDTO.getQuestionBody())
                .isAvailable(questionDTO.isAvailable())
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(levelService.getLevelByName(questionDTO.getLevel()))
                .module(moduleService.getModuleByName(questionDTO.getModule()))
                .build();
    }

    public QuestionDTO convertToDTO(Question question) {
        return QuestionDTO.builder()
                .body(question.getBody())
                .isAvailable(question.isAvailable())
                .creator(question.getCreator().getName())
                .level(question.getLevel().getName())
                .module(question.getModule().getName())
                .build();
    }

    private String getQuestionBody(Question question, QuestionDTO questionDTO) {
        if (questionDTO.getQuestionBody() == null) {
            return question.getBody();
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
