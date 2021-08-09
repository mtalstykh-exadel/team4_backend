package com.team4.testingsystem.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;

    private String currentDate;

    public ErrorResponse(String message) {
        this.message = message;
        this.currentDate = LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return currentDate;
    }

    public void setDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
