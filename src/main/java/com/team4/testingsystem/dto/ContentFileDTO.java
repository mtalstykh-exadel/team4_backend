package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ContentFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class ContentFileDTO implements Serializable {
    private Long id;
    private String url;
    private String topic;
    private String level;
    private List<QuestionDTO> questions;

    public ContentFileDTO(ContentFile contentFile) {
        id = contentFile.getId();
        url = contentFile.getUrl();
        topic = contentFile.getTopic();
        questions = contentFile.getQuestions().stream()
                .map(QuestionDTO::createWithCorrectAnswers)
                .collect(Collectors.toList());
    }

    public ContentFileDTO(ContentFile contentFile, String level) {
        this(contentFile);
        this.level = level;
    }

}
