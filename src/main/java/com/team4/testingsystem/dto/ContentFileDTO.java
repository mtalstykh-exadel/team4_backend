package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ContentFile;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContentFileDTO implements Serializable {
    private Long id;
    private String url;
    private String topic;
    private String level;
    private List<QuestionDTO> questions;

    public ContentFileDTO() {
    }
    public ContentFileDTO(ContentFile contentFile) {
        id = contentFile.getId();
        url = contentFile.getUrl();
        topic = contentFile.getTopic();
        level = contentFile.getQuestions().get(0).getLevel().getName();
        questions = contentFile.getQuestions().stream()
                .map(QuestionDTO::createWithCorrectAnswers)
                .collect(Collectors.toList());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<QuestionDTO> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentFileDTO that = (ContentFileDTO) o;
        return Objects.equals(id, that.id)
               && Objects.equals(url, that.url)
               && Objects.equals(topic, that.topic)
               && Objects.equals(level, that.level)
               && Objects.equals(questions,that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, topic, level, questions);
    }

}
