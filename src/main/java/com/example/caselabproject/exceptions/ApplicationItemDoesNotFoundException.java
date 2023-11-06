package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationItemDoesNotFoundException extends GlobalAppException{
    public ApplicationItemDoesNotFoundException() {
        super(404, "Application item does not found");
        log.warn(message);
    }
}
