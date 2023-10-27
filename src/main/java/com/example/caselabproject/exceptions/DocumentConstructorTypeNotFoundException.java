package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentConstructorTypeNotFoundException extends GlobalAppException {

    public DocumentConstructorTypeNotFoundException(Long id) {
        super(404, "Document type with " + id + " id doesn't exist");
        log.warn(message);
    }
}