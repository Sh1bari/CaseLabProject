package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentAccessException extends GlobalAppException {

    public DocumentAccessException(String username) {
        super(403, "User " + username + " does not have access to this document!");
        log.warn(message);
    }
}
