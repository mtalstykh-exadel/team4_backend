package com.team4.testingsystem.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TestsLimitExceededResponse implements Serializable {
    private String message;

    private String comeBackAfter;

    private String currentDate;

    public TestsLimitExceededResponse(String comeBackAfter) {
        this.message = "You can't start more than 3 tests per day. Please, try later";
        this.comeBackAfter = comeBackAfter;
        this.currentDate = LocalDateTime.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComeBackAfter() {
        return comeBackAfter;
    }

    public void setComeBackAfter(String comeBackAfter) {
        this.comeBackAfter = comeBackAfter;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
