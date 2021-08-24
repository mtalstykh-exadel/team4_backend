package com.team4.testingsystem.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TestsLimitExceededResponse implements Serializable {
    private String message;

    private String comeBackAfter;

    private String currentDate;

    public TestsLimitExceededResponse(String comeBackAfter) {
        this.message = "You can't start more than 3 tests per day. Please, try later";
        this.comeBackAfter = comeBackAfter;
        this.currentDate = LocalDateTime.now().toString();
    }

}
