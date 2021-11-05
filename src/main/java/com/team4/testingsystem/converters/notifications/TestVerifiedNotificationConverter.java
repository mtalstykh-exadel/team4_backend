package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.model.dto.ErrorReportAnswerDTO;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.services.ErrorReportAnswerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TestVerifiedNotificationConverter extends SingleNotificationConverter {
    private final ErrorReportAnswerService reportAnswerService;

    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        Long testId = notification.getTest().getId();
        List<ErrorReportAnswerDTO> reportAnswers = reportAnswerService.getReportsWithAnswersByTest(testId)
                .stream()
                .map(ErrorReportAnswerDTO::new)
                .collect(Collectors.toList());

        return notificationBuilder(notification)
                .level(notification.getTest().getLevel().getName())
                .reportAnswers(reportAnswers)
                .build();
    }

    @Override
    public NotificationType converterType() {
        return NotificationType.TEST_VERIFIED;
    }
}
