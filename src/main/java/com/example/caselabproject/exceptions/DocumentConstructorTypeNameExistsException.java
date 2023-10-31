package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentConstructorTypeNameExistsException extends GlobalAppException {

    public DocumentConstructorTypeNameExistsException(int status, String message) {
        super(status, message);
        log.warn(message);
    }
}
