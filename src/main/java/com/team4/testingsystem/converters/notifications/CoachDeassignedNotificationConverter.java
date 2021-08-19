package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.enums.NotificationType;

public class CoachDeassignedNotificationConverter extends SingleNotificationConverter {
    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        return notificationBuilder(notification)
                .level(notification.getTest().getLevel().getName())
                .build();
    }

    @Override
    public NotificationType converterType() {
        return NotificationType.COACH_DEASSIGNED;
    }
}
