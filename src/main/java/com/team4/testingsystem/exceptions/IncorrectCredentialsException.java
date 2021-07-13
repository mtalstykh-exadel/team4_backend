package com.team4.testingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Incorrect login or password")
public class IncorrectCredentialsException extends RuntimeException {
}
