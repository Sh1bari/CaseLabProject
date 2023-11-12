package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationCreateException extends GlobalAppException {
    public ApplicationCreateException() {
        super(500, "Could not create application");
        log.warn("Status: 500, Could not create application");
    }
}
