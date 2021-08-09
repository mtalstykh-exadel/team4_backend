package com.team4.testingsystem.services;

import com.team4.testingsystem.security.CustomUserDetails;

public interface AuthenticationService {

    CustomUserDetails authenticate(String username, String password);
}
