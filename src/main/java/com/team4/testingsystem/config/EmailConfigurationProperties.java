package com.team4.testingsystem.config;

import com.team4.testingsystem.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "email-notifications")
@Getter
@Setter
public class EmailConfigurationProperties {
    private Boolean enabled;
    private String fromEmail;
    private Map<NotificationType, String> templatePath = new HashMap<>();
}
