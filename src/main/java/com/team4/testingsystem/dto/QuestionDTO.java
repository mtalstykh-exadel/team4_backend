package com.team4.testingsystem.dto;

import java.util.Objects;

public class QuestionDTO {
    private String questionBody;
    private Boolean isAvailable;
    private String creator;
    private String level;
    private String module;

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

    public static class Builder {
        private QuestionDTO questionDTO;

        public Builder() {
            this.questionDTO = new QuestionDTO();
        }

        public Builder body(String body) {
            questionDTO.questionBody = body;
            return this;
        }

        public Builder isAvailable(boolean isAvailable) {
            questionDTO.isAvailable = isAvailable;
            return this;
        }

        public Builder creator(String creator) {
            questionDTO.creator = creator;
            return this;
        }

        public Builder level(String level) {
            questionDTO.level = level;
            return this;
        }

        public Builder module(String module) {
            questionDTO.module = module;
            return this;
        }

        public QuestionDTO build() {
            return questionDTO;
        }
    }
}
