package com.team4.testingsystem.converters;

import com.team4.testingsystem.converters.notifications.SingleNotificationConverter;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationConverter {
    private final Map<NotificationType, SingleNotificationConverter> converterByType;

    @Autowired
    public NotificationConverter(List<SingleNotificationConverter> converters) {
        converterByType = converters.stream()
                .collect(Collectors.toMap(SingleNotificationConverter::converterType, Function.identity()));
    }

    public NotificationDTO convertToDTO(Notification notification) {
        return converterByType.get(notification.getType()).convertToDTO(notification);
    }
}
