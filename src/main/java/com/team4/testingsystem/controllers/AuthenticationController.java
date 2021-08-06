package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.AuthenticationRequest;
import com.team4.testingsystem.dto.AuthenticationResponse;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.AuthenticationService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ApiOperation(value = "Heroku health check")
    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

    @ApiOperation(value = "Get current user's name")
    @GetMapping("/name")
    public String getName(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getName();
    }

    @ApiOperation(value = "Use it for authentication")
    @ApiResponse(code = 401, message = "Incorrect login or password")
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest credentials) {
        CustomUserDetails userDetails = authenticationService.authenticate(
                credentials.getLogin(),
                credentials.getPassword()
        );
        return new AuthenticationResponse(jwtTokenUtil.generateToken(userDetails), userDetails.getLanguage());
    }
}
