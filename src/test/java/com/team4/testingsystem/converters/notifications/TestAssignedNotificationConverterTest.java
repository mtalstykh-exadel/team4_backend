package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.model.entity.User;
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
public class TestAssignedNotificationConverterTest {
    @Mock
    private Notification notification;

    @Mock
    private com.team4.testingsystem.model.entity.Test test;

    @Mock
    private Level level;

    @Mock
    private User user;

    @InjectMocks
    private TestAssignedNotificationConverter converter;

    private static final Long NOTIFICATION_ID = 1L;
    private static final Long TEST_ID = 2L;
    private static final String LEVEL_NAME = Levels.A1.name();
    private static final String USER_EMAIL = "some@e.mail";
    private static final String USER_NAME = "user name";
    private static final String LANGUAGE = "rus";

    @Test
    public void convertToDTO() {
        Instant createdAt = Instant.now();
        Instant deadline = Instant.now().plusSeconds(1);

        Mockito.when(notification.getId()).thenReturn(NOTIFICATION_ID);
        Mockito.when(notification.getCreatedAt()).thenReturn(createdAt);
        Mockito.when(notification.getTest()).thenReturn(test);
        Mockito.when(notification.getType()).thenReturn(NotificationType.TEST_ASSIGNED);

        Mockito.when(notification.getUser()).thenReturn(user);
        Mockito.when(user.getLogin()).thenReturn(USER_EMAIL);
        Mockito.when(user.getName()).thenReturn(USER_NAME);
        Mockito.when(user.getLanguage()).thenReturn(LANGUAGE);

        Mockito.when(test.getId()).thenReturn(TEST_ID);
        Mockito.when(test.getDeadline()).thenReturn(deadline);
        Mockito.when(test.getLevel()).thenReturn(level);

        Mockito.when(level.getName()).thenReturn(LEVEL_NAME);

        NotificationDTO dto = converter.convertToDTO(notification);

        Assertions.assertEquals(NOTIFICATION_ID, dto.getId());
        Assertions.assertEquals(NotificationType.TEST_ASSIGNED, dto.getType());
        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(createdAt, dto.getCreatedAt());
        Assertions.assertEquals(LEVEL_NAME, dto.getLevel());
        Assertions.assertEquals(deadline, dto.getDeadline());
        Assertions.assertEquals(USER_EMAIL, dto.getUserEmail());
        Assertions.assertEquals(USER_NAME, dto.getUserName());
        Assertions.assertEquals(LANGUAGE, dto.getLanguage());

        Assertions.assertNull(dto.getFinishTime());
        Assertions.assertNull(dto.getPriority());
        Assertions.assertNull(dto.getReportAnswers());
    }

    @Test
    public void converterType() {
        Assertions.assertEquals(NotificationType.TEST_ASSIGNED, converter.converterType());
    }
}
