package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AuthenticationRequest;
import com.team4.testingsystem.exceptions.IncorrectCredentialsException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.AuthenticationService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private static final String JWT_TOKEN = "token";

    private CustomUserDetails userDetails;
    private String username;
    private String password;

    @BeforeEach
    void init() {
        userDetails = new CustomUserDetails(EntityCreatorUtil.createUser());
        username = userDetails.getUsername();
        password = userDetails.getPassword();
    }

    @Test
    void healthCheck() {
        Assertions.assertEquals("ok", authenticationController.healthCheck());
    }


    @Test
    void loginIncorrectCredentials() {
        Mockito.when(authenticationService.createAuthenticationToken(username, password))
                .thenThrow(new IncorrectCredentialsException());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        Assertions.assertThrows(IncorrectCredentialsException.class,
                () -> authenticationController.login(authenticationRequest));
    }

    @Test
    void loginSuccess() {
        Mockito.when(authenticationService.createAuthenticationToken(username, password)).thenReturn(JWT_TOKEN);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        Assertions.assertEquals(JWT_TOKEN, authenticationController.login(authenticationRequest));
    }
}
