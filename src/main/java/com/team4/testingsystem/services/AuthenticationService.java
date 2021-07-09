package com.team4.testingsystem.services;

import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 CustomUserDetailsService userDetailsService,
                                 JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Optional<String> createAuthenticationToken(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            return Optional.empty();
        }

        final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return Optional.of(jwtTokenUtil.generateToken(userDetails));
    }
}
