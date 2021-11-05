package com.team4.testingsystem.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.testingsystem.config.EmailConfigurationProperties;
import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.exceptions.EmailTemplateNotFoundException;
import com.team4.testingsystem.exceptions.EmailTemplateResolvingFailed;
import com.team4.testingsystem.services.EmailNotificationTemplateResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Service
@ConditionalOnProperty(prefix = "email-notifications", name = "enabled", havingValue = "true")
public class EmailNotificationTemplateResolverImpl implements EmailNotificationTemplateResolver {

    private final EmailConfigurationProperties properties;
    private final Configuration freemarkerConfiguration;
    private final ObjectMapper objectMapper;

    private final Map<NotificationType, Template> notificationTemplates = new HashMap<>();

    @Autowired
    public EmailNotificationTemplateResolverImpl(EmailConfigurationProperties properties,
                                                 Configuration freemarkerConfiguration,
                                                 ObjectMapper objectMapper) {
        this.properties = properties;
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        properties.getTemplatePath().keySet().forEach(this::registerTemplate);
    }

    @Override
    public String resolve(NotificationDTO notification) {
        Map<String, Object> parameters = objectMapper.convertValue(notification, new TypeReference<>() {});

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    notificationTemplates.get(notification.getType()),
                    parameters
            );
        } catch (IOException | TemplateException e) {
            throw new EmailTemplateResolvingFailed(notification.getType());
        }
    }

    public void registerTemplate(NotificationType type) {
        String templatePath = properties.getTemplatePath().get(type);
        try {
            notificationTemplates.put(type, freemarkerConfiguration.getTemplate(templatePath));
        } catch (IOException e) {
            throw new EmailTemplateNotFoundException(type);
        }
    }
}
