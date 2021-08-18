package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class TestAssignedNotificationConverter implements SingleNotificationConverter {
    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(NotificationType.TEST_ASSIGNED)
                .testId(notification.getTest().getId())
                .createdAt(notification.getCreatedAt())
                .level(notification.getTest().getLevel().getName())
                .deadline(notification.getTest().getDeadline())
                .build();
    }

    @Override
    public NotificationType converterType() {
        return NotificationType.TEST_ASSIGNED;
    }
}
