package com.team4.testingsystem.exceptions;

public class UserRoleNotFoundException extends NotFoundException {
    public UserRoleNotFoundException() {
        super("User role not found");
    }
}
