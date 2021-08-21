package com.team4.testingsystem.config;

import com.team4.testingsystem.enums.NotificationType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "email-notifications")
public class EmailConfigurationProperties {
    private Boolean enabled;
    private Map<NotificationType, String> templatePath = new HashMap<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<NotificationType, String> getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(Map<NotificationType, String> templatePath) {
        this.templatePath = templatePath;
    }
}
