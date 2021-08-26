package com.team4.testingsystem.handlers;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.services.EmailNotificationTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@ConditionalOnProperty(prefix = "email-notifications", name = "enabled", havingValue = "true")
public class EmailNotificationEventHandler {

    private final EmailNotificationTemplateResolver templateResolver;
    private final JavaMailSender mailSender;

    @Value("${email-notifications.from-email}")
    private String fromEmail;

    private final Map<NotificationType, String> emailSubjects = Map.of(
            NotificationType.TEST_ASSIGNED, "New test was assigned for you",
            NotificationType.TEST_DEASSIGNED, "Your were deassigned from the previous test",
            NotificationType.TEST_VERIFIED, "Your test was verified by the coach",
            NotificationType.COACH_ASSIGNED, "You were assigned for test verification",
            NotificationType.COACH_DEASSIGNED, "You were deassigned from the test verification",
            NotificationType.TEST_EXPIRED, "Test, assigned for you, has expired"
    );

    @Autowired
    public EmailNotificationEventHandler(EmailNotificationTemplateResolver templateResolver,
                                         JavaMailSender mailSender) {
        this.templateResolver = templateResolver;
        this.mailSender = mailSender;
    }

    @EventListener
    public void onApplicationEvent(NotificationDTO notificationDTO) throws MessagingException {
        if (!emailSubjects.containsKey(notificationDTO.getType())) {
            return;
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setFrom(fromEmail);
        message.setTo(notificationDTO.getUserEmail());
        message.setSubject(emailSubjects.get(notificationDTO.getType()));
        message.setText(templateResolver.resolve(notificationDTO), true);

        mailSender.send(mimeMessage);
    }
}
