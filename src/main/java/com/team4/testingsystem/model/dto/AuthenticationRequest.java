package com.team4.testingsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationRequest implements Serializable {
    private String login;
    private String password;

}
