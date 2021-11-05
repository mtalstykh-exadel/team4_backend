package com.team4.testingsystem.services;

import com.team4.testingsystem.model.dto.NotificationDTO;

public interface EmailNotificationTemplateResolver {
    String resolve(NotificationDTO notification);
}
