package com.team4.testingsystem.entities;

import com.team4.testingsystem.dto.QuestionDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ListeningTopicRequest implements Serializable {
    private String topic;
    private List<QuestionDTO> questions;

    public ListeningTopicRequest() {
    }

    public ListeningTopicRequest(String topic, List<QuestionDTO> questions) {
        this.topic = topic;
        this.questions = questions;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListeningTopicRequest that = (ListeningTopicRequest) o;
        return Objects.equals(topic, that.topic)
                && Objects.equals(questions, that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, questions);
    }
}
