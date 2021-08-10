package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.entities.Test test = EntityCreatorUtil.createTest(user, level);

        List<Question> questions = new ArrayList<>();
        for (Modules module : Modules.values()){
            Question question = EntityCreatorUtil.createQuestion(user);
            Module module1 = new Module();
            module1.setName(module.getName());
            question.setModule(module1);
            questions.add(question);
        }

        TestDTO testDTO = EntityCreatorUtil.createTestDTO(test);
        Mockito.when(questionService.getQuestionsByTestId(any())).thenReturn(questions);
        Mockito.when(contentFilesService.getContentFileByQuestionId(any()))
                .thenReturn(new ContentFile("url"));

        Assertions.assertEquals(testDTO, testConverter.convertToDTO(test));
    }
}
