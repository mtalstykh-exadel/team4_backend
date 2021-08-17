package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Notification;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    void create(NotificationType type, User user, Test test);

    List<Notification> getAllByUserId(Long userId);

    void remove(Long id);
}
