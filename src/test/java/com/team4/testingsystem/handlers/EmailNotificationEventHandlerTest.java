package com.team4.testingsystem.handlers;

import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.services.EmailNotificationTemplateResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailNotificationEventHandlerTest {
    @Mock
    private EmailNotificationTemplateResolver templateResolver;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationEventHandler notificationEventHandler;

    @Mock
    private NotificationDTO notificationDTO;

    @Mock
    private MimeMessage mimeMessage;

    private static final String EMAIL_TEXT = "raw email";
    private static final String FROM_EMAIL = "from@e.mail";
    private static final String USER_EMAIL = "some@e.mail";

    @Test
    void sendEmail() throws MessagingException {
        ReflectionTestUtils.setField(notificationEventHandler, "fromEmail", FROM_EMAIL);

        Mockito.when(notificationDTO.getUserEmail()).thenReturn(USER_EMAIL);
        Mockito.when(notificationDTO.getType()).thenReturn(NotificationType.TEST_ASSIGNED);
        Mockito.when(templateResolver.resolve(notificationDTO)).thenReturn(EMAIL_TEXT);
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        try (MockedConstruction<MimeMessageHelper> mock = Mockito.mockConstruction(MimeMessageHelper.class)) {
            notificationEventHandler.onApplicationEvent(notificationDTO);

            MimeMessageHelper helper = mock.constructed().get(0);
            Mockito.verify(helper).setFrom(FROM_EMAIL);
            Mockito.verify(helper).setTo(USER_EMAIL);
            Mockito.verify(helper).setSubject("New test was assigned for you");
            Mockito.verify(helper).setText(EMAIL_TEXT, true);
            Mockito.verify(mailSender).send(mimeMessage);
        }
    }

    @Test
    void sendEmailNoTopic() throws MessagingException {
        Mockito.when(notificationDTO.getType()).thenReturn(NotificationType.TEST_STARTED);

        notificationEventHandler.onApplicationEvent(notificationDTO);

        Mockito.verify(mailSender, Mockito.never()).send(Mockito.any(MimeMessage.class));
    }
}
