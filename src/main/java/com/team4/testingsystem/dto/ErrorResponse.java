package com.team4.testingsystem.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ErrorResponse implements Serializable {

    private String message;

    private String currentDate;

    public ErrorResponse(String message) {
        this.message = message;
        this.currentDate = LocalDateTime.now().toString();
    }

}
