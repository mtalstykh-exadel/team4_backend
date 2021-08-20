package com.team4.testingsystem.security;

import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import liquibase.ui.UIService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;

    private final User user = EntityCreatorUtil.createUser();

    @BeforeEach
    void init() {
        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }

    @Test
    void getId() {
        Assertions.assertEquals(user.getId(), customUserDetails.getId());
    }

    @Test
    void getName() {
        Assertions.assertEquals(user.getName(), customUserDetails.getName());
    }

    @Test
    void getRoleName() {
        Assertions.assertEquals(user.getRole().getRoleName(), customUserDetails.getRoleName());
    }

    @Test
    void getAvatar() {
        Assertions.assertEquals(user.getAvatar(), customUserDetails.getAvatar());
    }

    @Test
    void getLanguage() {
        Assertions.assertEquals(user.getLanguage(), customUserDetails.getLanguage());
    }
}