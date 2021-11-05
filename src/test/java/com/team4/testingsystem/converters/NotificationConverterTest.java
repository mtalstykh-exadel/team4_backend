package com.team4.testingsystem.converters;

import com.team4.testingsystem.converters.notifications.SingleNotificationConverter;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.enums.NotificationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class NotificationConverterTest {

    private NotificationConverter converter;

    @BeforeEach
    void init() {
        List<SingleNotificationConverter> notificationConverters = new ArrayList<>();

        for (NotificationType type : NotificationType.values()) {
            notificationConverters.add(new SingleNotificationConverter() {
                @Override
                public NotificationDTO convertToDTO(Notification notification) {
                    return NotificationDTO.builder().type(type).build();
                }

                @Override
                public NotificationType converterType() {
                    return type;
                }
            });
        }

        converter = new NotificationConverter(notificationConverters);
    }

    @Test
    void convertToDTO() {
        for (NotificationType type : NotificationType.values()) {
            Notification notification = Notification.builder().type(type).build();

            NotificationDTO notificationDTO = converter.convertToDTO(notification);
            Assertions.assertEquals(type, notificationDTO.getType());
        }
    }
}
