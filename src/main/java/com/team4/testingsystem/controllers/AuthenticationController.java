package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AuthenticationRequest;
import com.team4.testingsystem.services.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "Heroku health check")
    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

    @ApiOperation(value = "Use it for authentication")
    @ApiResponse(code = 200, message = "JWT token, use it for other requests")
    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest credentials) {
        return authenticationService.createAuthenticationToken(
                credentials.getLogin(),
                credentials.getPassword()
        );
    }
}
