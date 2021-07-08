package com.team4.testingsystem.services;

import com.team4.testingsystem.security.CustomUserDetails;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private CustomUserDetails userDetails;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String JWT_TOKEN = "jwt_token";

    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void init() {
        token = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
    }

    @Test
    void incorrectCredentials() {
        Mockito.when(authenticationManager.authenticate(token)).thenThrow(new BadCredentialsException(""));

        Assertions.assertTrue(authenticationService.createAuthenticationToken(USERNAME, PASSWORD).isEmpty());
    }

    @Test
    void correctToken() {
        Mockito.when(authenticationManager.authenticate(token)).thenReturn(token);
        Mockito.when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn(JWT_TOKEN);

        Optional<String> jwt_token = authenticationService.createAuthenticationToken(USERNAME, PASSWORD);

        Assertions.assertFalse(jwt_token.isEmpty());
        Assertions.assertEquals(JWT_TOKEN, jwt_token.get());
    }
}
