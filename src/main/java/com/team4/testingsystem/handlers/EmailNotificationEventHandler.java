package com.team4.testingsystem.handlers;

import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.services.EmailNotificationTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@ConditionalOnProperty(prefix = "email-notifications", name = "enabled", havingValue = "true")
public class EmailNotificationEventHandler {

    private final EmailNotificationTemplateResolver templateResolver;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailNotificationEventHandler(EmailNotificationTemplateResolver templateResolver,
                                         JavaMailSender mailSender) {
        this.templateResolver = templateResolver;
        this.mailSender = mailSender;
    }

    @EventListener
    public void onApplicationEvent(NotificationDTO notificationDTO) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setFrom("untitled.noreply@gmail.com");
        message.setTo(notificationDTO.getUserEmail());
        message.setSubject("Untitled testing system");
        message.setText(templateResolver.resolve(notificationDTO), true);

        mailSender.send(mimeMessage);
    }
}
