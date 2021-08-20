package com.team4.testingsystem.utils.jwt;

import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilTest {
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private CustomUserDetails incorrectUserDetails;

    private static final String USERNAME = "username";
    private static final String INCORRECT_USERNAME = "incorrect_username";

    @BeforeEach
    void init() {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    void tryExtractUsernameInvalidToken() {
        Assertions.assertTrue(jwtTokenUtil.tryExtractUsername("token").isEmpty());
    }

    @Test
    void tryExtractUsernameValid() {
        Mockito.when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtTokenUtil.generateToken(userDetails);
        Assertions.assertFalse(jwtTokenUtil.tryExtractUsername(token).isEmpty());
        Assertions.assertEquals(USERNAME, jwtTokenUtil.tryExtractUsername(token).get());
    }

    @Test
    void correctTokenParameters() {
        Mockito.when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtTokenUtil.generateToken(userDetails);
        Assertions.assertEquals(userDetails.getUsername(), jwtTokenUtil.extractUsername(token));
        Assertions.assertFalse(jwtTokenUtil.isTokenExpired(token));
    }

    @Test
    void validToken() {
        Mockito.when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtTokenUtil.generateToken(userDetails);
        Assertions.assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void incorrectUsernameToken() {
        Mockito.when(userDetails.getUsername()).thenReturn(USERNAME);
        Mockito.when(incorrectUserDetails.getUsername()).thenReturn(INCORRECT_USERNAME);

        String token = jwtTokenUtil.generateToken(userDetails);
        Assertions.assertFalse(jwtTokenUtil.validateToken(token, incorrectUserDetails));
    }

    @Test
    void isTokenExpired() {
        String token = jwtTokenUtil.generateToken(userDetails);

        Assertions.assertFalse(jwtTokenUtil.isTokenExpired(token));
    }


    @Test
    void validateToken() {
        userDetails = new CustomUserDetails(EntityCreatorUtil.createUser());
        JwtTokenUtil jwtTokenUtil = Mockito.spy(new JwtTokenUtil());
        String token = jwtTokenUtil.generateToken(userDetails);
        Mockito.doThrow(JwtException.class)
                .when(jwtTokenUtil).extractUsername(token);

        Assertions.assertFalse(jwtTokenUtil.validateToken(token, userDetails));
    }
}
