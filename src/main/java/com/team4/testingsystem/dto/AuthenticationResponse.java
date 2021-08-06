package com.team4.testingsystem.dto;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
    private String token;
    private String language;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String language) {
        this.token = token;
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
