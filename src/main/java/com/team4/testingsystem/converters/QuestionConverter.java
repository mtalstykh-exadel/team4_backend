package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.services.impl.LevelServiceImpl;
import com.team4.testingsystem.services.impl.ModuleServiceImpl;
import com.team4.testingsystem.services.impl.UsersServiceImpl;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

    private final LevelServiceImpl levelService;
    private final ModuleServiceImpl moduleService;
    private final UsersServiceImpl usersService;

    @Autowired
    public QuestionConverter(LevelServiceImpl levelService, ModuleServiceImpl moduleService, UsersServiceImpl usersService) {
        this.levelService = levelService;
        this.moduleService = moduleService;
        this.usersService = usersService;
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

}
