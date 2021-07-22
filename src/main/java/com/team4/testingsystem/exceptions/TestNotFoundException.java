package com.team4.testingsystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Test not found")
public class TestNotFoundException extends NotFoundException {
}
