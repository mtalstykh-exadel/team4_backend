package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.QuestionDTO;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.LevelService;
import com.team4.testingsystem.services.ModuleService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.services.UsersService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class QuestionConverterTest {
    @Mock
    private LevelService levelService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ModuleService moduleService;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private QuestionConverter questionConverter;

    @Test
    void convertToEntityWithId() {
        User user = EntityCreatorUtil.createUser();
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        try(MockedStatic<JwtTokenUtil> jwtTokenUtilMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)){
            jwtTokenUtilMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(questionService.getById(question.getId())).thenReturn(question);
            Mockito.when(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId())).thenReturn(user);
            Mockito.when(levelService.getLevelByName(questionDTO.getLevel())).thenReturn(question.getLevel());
            Mockito.when(moduleService.getModuleByName(questionDTO.getModule())).thenReturn(question.getModule());

            Question result = questionConverter.convertToEntity(questionDTO, question.getId());
            result.setAvailable(question.isAvailable());
            result.setId(question.getId());
            Assertions.assertEquals(question, result);
        }
    }

    @Test
    void convertToEntity() {
        User user = EntityCreatorUtil.createUser();
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        try(MockedStatic<JwtTokenUtil> jwtTokenUtilMockedStatic = Mockito.mockStatic(JwtTokenUtil.class)){
            jwtTokenUtilMockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId())).thenReturn(user);
            Mockito.when(levelService.getLevelByName(questionDTO.getLevel())).thenReturn(question.getLevel());
            Mockito.when(moduleService.getModuleByName(questionDTO.getModule())).thenReturn(question.getModule());

            Question result = questionConverter.convertToEntity(questionDTO);
            result.setAvailable(question.isAvailable());
            result.setId(question.getId());
            Assertions.assertEquals(question, result);
        }
    }

    @Test
    void convertToDTO() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        QuestionDTO result = QuestionDTO.create(question);
        Assertions.assertEquals(questionDTO, result);
    }
}
