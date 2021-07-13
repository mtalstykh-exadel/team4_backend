package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.User;

public class QuestionDTO {
    private String questionBody;
    private boolean isAvailable;
    private Long creatorId;
    private Long levelId;
    private Long moduleId;


    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public static class Builder {
        private QuestionDTO questionDTO;

        public Builder() {
            this.questionDTO = new QuestionDTO();
        }

        public Builder questionBody(String questionBody) {
            questionDTO.questionBody = questionBody;
            return this;
        }

        public Builder isAvailable(boolean isAvailable) {
            questionDTO.isAvailable = isAvailable;
            return this;
        }

        public Builder creatorId(User user){
            questionDTO.creatorId = user.getId();
            return  this;
        }

        public Builder levelId(Level level) {
            questionDTO.levelId = level.getId();
            return this;
        }

        public Builder moduleId(Module module) {
            questionDTO.moduleId = module.getId();
            return this;
        }

        public QuestionDTO build() {
            return questionDTO;
        }
    }
}
