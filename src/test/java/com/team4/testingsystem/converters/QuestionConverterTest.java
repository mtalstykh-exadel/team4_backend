package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.impl.LevelServiceImpl;
import com.team4.testingsystem.services.impl.ModuleServiceImpl;
import com.team4.testingsystem.services.impl.QuestionServiceImpl;
import com.team4.testingsystem.services.impl.UsersServiceImpl;
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
    private LevelServiceImpl levelService;

    @Mock
    private QuestionServiceImpl questionService;

    @Mock
    private ModuleServiceImpl moduleService;

    @Mock
    private UsersServiceImpl usersService;

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
            Mockito.when(questionService.getQuestionById(question.getId())).thenReturn(question);
            Mockito.when(usersService.getUserById(JwtTokenUtil.extractUserDetails().getId())).thenReturn(user);
            Mockito.when(levelService.getLevelByName(questionDTO.getLevel())).thenReturn(question.getLevel());
            Mockito.when(moduleService.getModuleByName(questionDTO.getModule())).thenReturn(question.getModule());

            Question result = questionConverter.convertToEntity(questionDTO, question.getId());
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
            result.setId(question.getId());
            Assertions.assertEquals(question, result);
        }
    }

    @Test
    void convertToDTO() {
        Question question = EntityCreatorUtil.createQuestion();
        QuestionDTO questionDTO = EntityCreatorUtil.createQuestionDto();
        QuestionDTO result = questionConverter.convertToDTO(question);
        Assertions.assertEquals(questionDTO, result);
    }
}