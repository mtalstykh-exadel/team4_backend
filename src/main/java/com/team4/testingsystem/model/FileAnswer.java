package com.team4.testingsystem.model;

public class FileAnswer {
    private int id;
    private int questionID;
    private String url;

    public FileAnswer(int id, int questionID, String url) {
        this.id = id;
        this.questionID = questionID;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FileAnswer{" +
                "id=" + id +
                ", questionID=" + questionID +
                ", url='" + url + '\'' +
                '}';
    }
}
