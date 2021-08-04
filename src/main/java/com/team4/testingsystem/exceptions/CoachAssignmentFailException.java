package com.team4.testingsystem.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Coach can't verify his own test")
public class CoachAssignmentFailException extends  RuntimeException{
}
