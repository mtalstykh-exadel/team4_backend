package com.team4.testingsystem.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;

    private String date;

    public ErrorResponse(String message) {
        this.message = message;
        this.date = LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
