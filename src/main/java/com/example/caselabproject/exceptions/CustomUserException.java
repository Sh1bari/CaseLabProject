package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserException extends GlobalAppException {

    public CustomUserException() {
        super(404, "No users found with the given criteria");
        log.warn(message);
    }
}