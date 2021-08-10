package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.IncorrectCredentialsException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.CustomUserDetailsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private CustomUserDetails userDetails;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void init() {
        token = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
    }

    @Test
    void incorrectCredentials() {
        Mockito.when(authenticationManager.authenticate(token)).thenThrow(new BadCredentialsException(""));

        Assertions.assertThrows(IncorrectCredentialsException.class,
                () -> authenticationService.authenticate(USERNAME, PASSWORD));
    }

    @Test
    void correctToken() {
        Mockito.when(authenticationManager.authenticate(token)).thenReturn(token);
        Mockito.when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        Assertions.assertEquals(userDetails, authenticationService.authenticate(USERNAME, PASSWORD));
    }
}
