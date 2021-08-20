package com.team4.testingsystem.exceptions;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException() {
        super("Notification not found");
    }
}
