package com.team4.testingsystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "File answer not found")
public class FileAnswerNotFoundException extends NotFoundException {
}
