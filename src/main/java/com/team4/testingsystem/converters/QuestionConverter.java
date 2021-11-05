package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.QuestionDTO;
import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Module;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.ModuleService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class QuestionConverter {

    private final LevelService levelService;
    private final QuestionService questionService;
    private final ModuleService moduleService;
    private final UsersService usersService;

    public Question convertToEntity(QuestionDTO questionDTO, Long id) {
        Question question = questionService.getById(id);
        Question result = Question.builder()
                .body(getQuestionBody(question, questionDTO))
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(getLevel(question, questionDTO))
                .module(getModule(question, questionDTO))
                .isAvailable(true)
                .build();
        if (questionDTO.getAnswers() != null) {
            questionService.addAnswers(result, questionDTO.getAnswers());
        }
        return result;
    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        Question result = Question.builder()
                .body(questionDTO.getQuestionBody())
                .creator(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId()))
                .level(levelService.getLevelByName(questionDTO.getLevel()))
                .module(moduleService.getModuleByName(questionDTO.getModule()))
                .build();
        if (questionDTO.getAnswers() != null) {
            questionService.addAnswers(result, questionDTO.getAnswers());
        }
        return result;
    }

    private String getQuestionBody(Question question, QuestionDTO questionDTO) {
        if (questionDTO.getQuestionBody() == null) {
            return question.getBody();
        }
        return questionDTO.getQuestionBody();
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
