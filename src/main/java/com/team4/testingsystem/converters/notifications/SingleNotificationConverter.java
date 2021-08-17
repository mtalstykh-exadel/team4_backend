package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.enums.NotificationType;

public interface SingleNotificationConverter {

    NotificationDTO convertToDTO(Notification notification);

    NotificationType converterType();
}
