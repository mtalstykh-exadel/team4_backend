package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Grade already exists")
public class CoachGradeAlreadyExistsException extends RuntimeException {
}