package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.enums.NotificationType;

public abstract class SingleNotificationConverter {

    protected NotificationDTO.Builder notificationBuilder(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(notification.getType())
                .testId(notification.getTest().getId())
                .createdAt(notification.getCreatedAt())
                .userEmail(notification.getUser().getLogin())
                .userName(notification.getUser().getName());
    }

    public abstract NotificationDTO convertToDTO(Notification notification);

    public abstract NotificationType converterType();
}
