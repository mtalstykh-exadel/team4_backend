package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.exceptions.IncorrectCredentialsException;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.AuthenticationService;
import com.team4.testingsystem.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    @Override
    public CustomUserDetails authenticate(String username, String password) throws IncorrectCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException();
        }

        return (CustomUserDetails) userDetailsService.loadUserByUsername(username);
    }
}
