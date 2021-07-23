package com.team4.testingsystem.dto;

import java.io.Serializable;

public class FileAnswerRequest implements Serializable {

    private long questionId;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FileAnswerRequest fileAnswerRequest;

        public Builder() {
            this.fileAnswerRequest = new FileAnswerRequest();
        }

        public Builder questionId(long questionId) {
            fileAnswerRequest.questionId = questionId;
            return this;
        }

        public Builder url(String url) {
            fileAnswerRequest.url = url;
            return this;
        }

        public FileAnswerRequest build() {
            return fileAnswerRequest;
        }
    }
}
