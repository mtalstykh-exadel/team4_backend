package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AuthenticationRequest;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/name")
    public String getName(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getName();
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest credentials) {
        return authenticationService.createAuthenticationToken(
                credentials.getLogin(),
                credentials.getPassword()
        );
    }
}
