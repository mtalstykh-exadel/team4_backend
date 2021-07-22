package com.team4.testingsystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Question not found")
public class QuestionNotFoundException extends NotFoundException{
}
