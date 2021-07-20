package com.team4.testingsystem.dto;

import java.io.Serializable;

public class ContentFileRequest implements Serializable {

    private String url;
    private Long questionId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
