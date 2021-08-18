package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.NotificationNotFoundException;
import com.team4.testingsystem.repositories.NotificationRepository;

import java.security.AccessControlException;
import java.util.List;
import java.util.Optional;

import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private User user;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private Notification notification;

    private static final Long NOTIFICATION_ID = 1L;
    private static final Long GOOD_USER_ID = 2L;
    private static final Long BAD_USER_ID = 42L;

    @Test
    public void create() {
        notificationService.create(NotificationType.TEST_ASSIGNED, user, test);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(notificationRepository).save(captor.capture());

        Assertions.assertEquals(NotificationType.TEST_ASSIGNED, captor.getValue().getType());
        Assertions.assertEquals(user, captor.getValue().getUser());
        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertNotNull(captor.getValue().getCreatedAt());
    }

    @Test
    public void getAllByUserId() {
        List<Notification> expected = List.of(new Notification());
        Mockito.when(notificationRepository.getAllByUserId(NOTIFICATION_ID)).thenReturn(expected);

        Assertions.assertEquals(expected, notificationService.getAllByUserId(NOTIFICATION_ID));
    }

    @Test
    public void removeByIdNotFound() {
        Mockito.when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotificationNotFoundException.class,
                () -> notificationService.removeById(NOTIFICATION_ID));
    }

    @Test
    public void removeByIdIncorrectUser() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(BAD_USER_ID);

            Mockito.when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
            Mockito.when(notification.getUser()).thenReturn(user);
            Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

            Assertions.assertThrows(AccessControlException.class,
                    () -> notificationService.removeById(NOTIFICATION_ID));
        }
    }

    @Test
    public void removeByIdSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(GOOD_USER_ID);

            Mockito.when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
            Mockito.when(notification.getUser()).thenReturn(user);
            Mockito.when(user.getId()).thenReturn(GOOD_USER_ID);

            notificationService.removeById(NOTIFICATION_ID);

            Mockito.verify(notificationRepository).delete(notification);
        }
    }
}
