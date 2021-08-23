package com.team4.testingsystem.exceptions;

import com.team4.testingsystem.enums.NotificationType;

public class EmailTemplateNotFoundException extends NotFoundException {
    public EmailTemplateNotFoundException(NotificationType type) {
        super("Email template for " + type.name() + " notification not found");
    }
}
