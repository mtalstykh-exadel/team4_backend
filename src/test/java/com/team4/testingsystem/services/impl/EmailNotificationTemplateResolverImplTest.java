package com.team4.testingsystem.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.config.EmailConfigurationProperties;
import com.team4.testingsystem.dto.NotificationDTO;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.EmailTemplateNotFoundException;
import com.team4.testingsystem.exceptions.EmailTemplateResolvingFailed;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EmailNotificationTemplateResolverImplTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmailConfigurationProperties properties;

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private Template template;

    @InjectMocks
    private EmailNotificationTemplateResolverImpl templateResolver;

    private static final String TEMPLATE_PATH = "path";
    private static final String RESOLVED_VALUE = "resolved";

    @Test
    void resolveKeyNotFound() {
        Mockito.when(properties.getTemplatePath()).thenReturn(Map.of());

        Assertions.assertThrows(EmailTemplateNotFoundException.class,
                () -> templateResolver.registerTemplate(NotificationType.TEST_ASSIGNED));
    }

    @Test
    void resolveTemplateNotFound() throws IOException {
        Mockito.when(properties.getTemplatePath())
                .thenReturn(Map.of(NotificationType.TEST_ASSIGNED, TEMPLATE_PATH));
        Mockito.when(freemarkerConfiguration.getTemplate(TEMPLATE_PATH)).thenThrow(IOException.class);

        Assertions.assertThrows(EmailTemplateNotFoundException.class,
                () -> templateResolver.registerTemplate(NotificationType.TEST_ASSIGNED));
    }

    @Test
    void resolveProcessTemplateFailed() throws IOException {
        Mockito.when(properties.getTemplatePath())
                .thenReturn(Map.of(NotificationType.TEST_ASSIGNED, TEMPLATE_PATH));
        Mockito.when(freemarkerConfiguration.getTemplate(TEMPLATE_PATH)).thenReturn(template);

        templateResolver.registerTemplate(NotificationType.TEST_ASSIGNED);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .type(NotificationType.TEST_ASSIGNED)
                .build();

        Mockito.when(objectMapper.convertValue(Mockito.eq(notificationDTO), Mockito.any(TypeReference.class)))
                .thenReturn(Map.of());

        try (MockedStatic<FreeMarkerTemplateUtils> mockUtils = Mockito.mockStatic(FreeMarkerTemplateUtils.class)) {
            mockUtils.when(() -> FreeMarkerTemplateUtils.processTemplateIntoString(template, Map.of()))
                    .thenThrow(TemplateException.class);

            Assertions.assertThrows(EmailTemplateResolvingFailed.class,
                    () -> templateResolver.resolve(notificationDTO));
        }
    }

    @Test
    void resolveSuccess() throws IOException {
        Mockito.when(properties.getTemplatePath())
                .thenReturn(Map.of(NotificationType.TEST_ASSIGNED, TEMPLATE_PATH));
        Mockito.when(freemarkerConfiguration.getTemplate(TEMPLATE_PATH)).thenReturn(template);

        templateResolver.registerTemplate(NotificationType.TEST_ASSIGNED);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .type(NotificationType.TEST_ASSIGNED)
                .build();

        Mockito.when(objectMapper.convertValue(Mockito.eq(notificationDTO), Mockito.any(TypeReference.class)))
                .thenReturn(Map.of());

        try (MockedStatic<FreeMarkerTemplateUtils> mockUtils = Mockito.mockStatic(FreeMarkerTemplateUtils.class)) {
            mockUtils.when(() -> FreeMarkerTemplateUtils.processTemplateIntoString(template, Map.of()))
                    .thenReturn(RESOLVED_VALUE);

            Assertions.assertEquals(RESOLVED_VALUE, templateResolver.resolve(notificationDTO));
        }
    }
}
