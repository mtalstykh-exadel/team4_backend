package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.converters.NotificationConverter;
import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.NotificationNotFoundException;
import com.team4.testingsystem.repositories.NotificationRepository;
import com.team4.testingsystem.services.NotificationService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void create(NotificationType type, User user, Test test) {
        Notification notification = Notification.builder()
                .type(type)
                .user(user)
                .test(test)
                .createdAt(Instant.now())
                .build();
        notification = notificationRepository.save(notification);

        eventPublisher.publishEvent(notificationConverter.convertToDTO(notification));
    }

    @Override
    public List<Notification> getAllByUserId(Long userId) {
        return notificationRepository.getAllByUserId(userId);
    }

    @Override
    public void removeById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        Long userId = JwtTokenUtil.extractUserDetails().getId();
        if (!notification.getUser().getId().equals(userId)) {
            throw new AccessControlException("You don't have access to this notification");
        }

        notificationRepository.delete(notification);
    }
}
