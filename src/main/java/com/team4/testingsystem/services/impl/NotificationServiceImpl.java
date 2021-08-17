package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.NotificationNotFoundException;
import com.team4.testingsystem.repositories.NotificationRepository;
import com.team4.testingsystem.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void create(NotificationType type, User user, Test test) {
        Notification notification = Notification.builder()
                .type(type)
                .user(user)
                .test(test)
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllByUserId(Long userId) {
        return notificationRepository.getAllByUserId(userId);
    }

    @Override
    public void remove(Long id) {
        if (notificationRepository.removeById(id) == 0) {
            throw new NotificationNotFoundException();
        }
    }
}
