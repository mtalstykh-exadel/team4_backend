package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.NotificationConverter;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.services.NotificationService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationConverter converter;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationConverter converter) {
        this.notificationService = notificationService;
        this.converter = converter;
    }

    @ApiOperation(value = "Get all notifications for current user")
    @GetMapping("/")
    public List<NotificationDTO> getNotifications() {
        Long userId = JwtTokenUtil.extractUserDetails().getId();
        return notificationService.getAllByUserId(userId).stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Remove notification by ID")
    @DeleteMapping("/{id}")
    public void removeById(@PathVariable Long id) {
        notificationService.removeById(id);
    }
}
