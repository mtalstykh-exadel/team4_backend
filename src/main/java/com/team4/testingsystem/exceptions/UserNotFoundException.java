package com.team4.testingsystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User not found")
public class UserNotFoundException extends NotFoundException {
}
