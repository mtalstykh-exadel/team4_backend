package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContentFileConverterTest {

    private static final String TOPIC = "topic";
    private static final String URL = "https://best_listening_audios.com/";

    @Mock
    private QuestionConverter questionConverter;

    @InjectMocks
    private ContentFileConverter contentFileConverter;

    @Test
    void convertToEntity() {
        Mockito.when(questionConverter.convertToEntity(EntityCreatorUtil.createQuestionDto()))
                .thenReturn(EntityCreatorUtil.createQuestion());
        ContentFile contentFile = new ContentFile();
        contentFile.setTopic(TOPIC);
        contentFile.setUrl(URL);
        contentFile.setQuestions(List.of(EntityCreatorUtil.createQuestion()));
        ContentFileDTO contentFileDTO = new ContentFileDTO(contentFile);

        Assertions.assertEquals(contentFile,
                contentFileConverter.convertToEntity(contentFileDTO));
    }
}