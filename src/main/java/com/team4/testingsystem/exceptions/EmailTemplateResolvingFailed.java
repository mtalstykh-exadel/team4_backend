package com.team4.testingsystem.exceptions;

import com.team4.testingsystem.enums.NotificationType;

public class EmailTemplateResolvingFailed extends ConflictException {
    public EmailTemplateResolvingFailed(NotificationType type) {
        super("Failed to resolve email template of type " + type.name());
    }
}
