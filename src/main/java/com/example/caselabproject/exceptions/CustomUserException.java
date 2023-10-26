package com.example.caselabproject.exceptions;

public class CustomUserException extends GlobalAppException {

    public CustomUserException(int status, String message) {
        super(status, message);
    }
}