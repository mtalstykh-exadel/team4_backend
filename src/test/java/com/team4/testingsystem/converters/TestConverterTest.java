package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import liquibase.pro.packaged.E;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TestConverterTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private ContentFilesService contentFilesService;

    @InjectMocks
    private TestConverter testConverter;

    @Test
    void convertToDTO() {
        User user = EntityCreatorUtil.createUser();
        Question question = EntityCreatorUtil.createQuestion(user);
        List<Question> questions = new ArrayList<>();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user);
        TestDTO testDTO = EntityCreatorUtil.createTestDTO(test);
        Mockito.when(questionService
                .getQuestionByTestIdAndModule(any(),any())).thenReturn(question);
        Mockito.when(questionService
                .getQuestionsByTestIdAndModule(any(),any())).thenReturn(questions);
        Mockito.when(contentFilesService
                .getContentFileByQuestionId(any())).thenReturn(new ContentFile());
        Assertions.assertEquals(testDTO , testConverter.convertToDTO(test));
    }
}