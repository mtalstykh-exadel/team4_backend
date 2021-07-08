package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.UserEntity;
import com.team4.testingsystem.entities.UserRoleEntity;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserRoleEntity userRoleEntity;

    @Mock
    private UserEntity userEntity;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ROLE_NAME = "ROLE_TEST";

    @Test
    void userDoesNotExist() {
        Mockito.when(usersRepository.findByLogin(USERNAME)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    void correctUserDetails() {
        Mockito.when(userRoleEntity.getRoleName()).thenReturn(ROLE_NAME);

        Mockito.when(userEntity.getLogin()).thenReturn(USERNAME);
        Mockito.when(userEntity.getPassword()).thenReturn(PASSWORD);
        Mockito.when(userEntity.getRole()).thenReturn(userRoleEntity);

        Mockito.when(usersRepository.findByLogin(USERNAME)).thenReturn(Optional.of(userEntity));

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        Assertions.assertEquals(USERNAME, userDetails.getUsername());
        Assertions.assertEquals(PASSWORD, userDetails.getPassword());

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Assertions.assertEquals(Collections.singletonList(ROLE_NAME), authorities);
    }
}
