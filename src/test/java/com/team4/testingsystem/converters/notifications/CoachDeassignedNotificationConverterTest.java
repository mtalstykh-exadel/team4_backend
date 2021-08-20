package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.NotificationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class CoachDeassignedNotificationConverterTest {
    @Mock
    private Notification notification;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private Level level;

    @InjectMocks
    private CoachDeassignedNotificationConverter converter;

    private static final Long NOTIFICATION_ID = 1L;
    private static final Long TEST_ID = 2L;
    private static final String LEVEL_NAME = Levels.A1.name();

    @Test
    public void convertToDTO() {
        Instant createdAt = Instant.now();

        Mockito.when(notification.getId()).thenReturn(NOTIFICATION_ID);
        Mockito.when(notification.getCreatedAt()).thenReturn(createdAt);
        Mockito.when(notification.getTest()).thenReturn(test);
        Mockito.when(notification.getType()).thenReturn(NotificationType.TEST_ASSIGNED);

        Mockito.when(test.getId()).thenReturn(TEST_ID);
        Mockito.when(test.getLevel()).thenReturn(level);

        Mockito.when(level.getName()).thenReturn(LEVEL_NAME);

        NotificationDTO dto = converter.convertToDTO(notification);

        Assertions.assertEquals(NOTIFICATION_ID, dto.getId());
        Assertions.assertEquals(NotificationType.TEST_ASSIGNED, dto.getType());
        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(createdAt, dto.getCreatedAt());
        Assertions.assertEquals(LEVEL_NAME, dto.getLevel());

        Assertions.assertNull(dto.getFinishTime());
        Assertions.assertNull(dto.getDeadline());
        Assertions.assertNull(dto.getPriority());
    }

    @Test
    public void converterType() {
        Assertions.assertEquals(NotificationType.COACH_DEASSIGNED, converter.converterType());
    }
}
