package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionDTO {
    private String questionBody;
    private Boolean isAvailable;
    private String creator;
    private String level;
    private String module;
    private List<String> answers;

    public QuestionDTO(Question question) {
        this.questionBody = question.getBody();
        this.isAvailable = question.isAvailable();
        this.creator = question.getCreator().getName();
        this.level = question.getLevel().getName();
        this.module = question.getModule().getName();
        this.answers = new ArrayList<>();
        question.getAnswers().forEach(answer -> answers.add(answer.getAnswerBody()));
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(questionBody, that.questionBody)
               && Objects.equals(isAvailable, that.isAvailable)
               && Objects.equals(creator, that.creator)
               && Objects.equals(level, that.level)
               && Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionBody, isAvailable, creator, level, module);
    }
}
