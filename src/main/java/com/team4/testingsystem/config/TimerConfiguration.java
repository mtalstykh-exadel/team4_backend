package com.team4.testingsystem.config;

import com.team4.testingsystem.services.TestsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimerConfiguration {

    final TestsService testsService;

    public TimerConfiguration(TestsService testsService) {
        this.testsService = testsService;
    }
    @Bean
    public void recoverTimers(){
        testsService.startAllTimers();
    }
}
