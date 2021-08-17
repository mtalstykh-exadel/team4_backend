package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.NotificationNotFoundException;
import com.team4.testingsystem.repositories.NotificationRepository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        Mockito.when(notificationRepository.getAllByUserId(1L)).thenReturn(expected);

        Assertions.assertEquals(expected, notificationService.getAllByUserId(1L));
    }

    @Test
    public void removeNotFound() {
        Mockito.when(notificationRepository.removeById(1L)).thenReturn(0);
        Assertions.assertThrows(NotificationNotFoundException.class, () -> notificationService.remove(1L));
    }

    @Test
    public void removeSuccess() {
        Mockito.when(notificationRepository.removeById(1L)).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> notificationService.remove(1L));
    }
}
