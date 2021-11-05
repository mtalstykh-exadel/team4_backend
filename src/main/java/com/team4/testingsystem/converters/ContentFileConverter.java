package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.ContentFileDTO;
import com.team4.testingsystem.model.dto.QuestionDTO;
import com.team4.testingsystem.model.entity.ContentFile;
import com.team4.testingsystem.model.entity.Question;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ContentFileConverter {
    private final QuestionConverter questionConverter;

    public ContentFile convertToEntity(ContentFileDTO contentFileDTO) {
        ContentFile contentFile = new ContentFile();
        contentFile.setQuestions(convertToQuestions(contentFileDTO.getQuestions()));
        contentFile.setTopic(contentFileDTO.getTopic());
        contentFile.setUrl(contentFileDTO.getUrl());
        return contentFile;
    }

    private List<Question> convertToQuestions(List<QuestionDTO> questionsDTO) {
        return questionsDTO.stream().map(questionConverter::convertToEntity).collect(Collectors.toList());
    }
}
