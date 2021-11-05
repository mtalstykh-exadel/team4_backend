package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class TestDeassignedNotificationConverter extends SingleNotificationConverter {
    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        return notificationBuilder(notification).build();
    }

    @Override
    public NotificationType converterType() {
        return NotificationType.TEST_DEASSIGNED;
    }
}
