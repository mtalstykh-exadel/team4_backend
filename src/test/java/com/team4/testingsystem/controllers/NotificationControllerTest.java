package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.NotificationConverter;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.NotificationService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {
    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationConverter notificationConverter;

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private Notification notification;

    @Mock
    private NotificationDTO notificationDTO;

    private static final Long USER_ID = 1L;

    @Test
    void getNotificationsSuccess() {
        try (MockedStatic<JwtTokenUtil> mockJwtTokenUtil = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockJwtTokenUtil.when(JwtTokenUtil::extractUserDetails).thenReturn(userDetails);
            Mockito.when(userDetails.getId()).thenReturn(USER_ID);

            Mockito.when(notificationService.getAllByUserId(USER_ID)).thenReturn(List.of(notification));
            Mockito.when(notificationConverter.convertToDTO(notification)).thenReturn(notificationDTO);

            Assertions.assertEquals(List.of(notificationDTO), notificationController.getNotifications());
        }
    }

    @Test
    void getNotificationSuccess() {
        notificationController.removeById(1L);
        Mockito.verify(notificationService).removeById(1L);
    }
}
