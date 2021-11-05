package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    void create(NotificationType type, User user, Test test);

    List<Notification> getAllByUserId(Long userId);

    void removeById(Long notificationId);
}
