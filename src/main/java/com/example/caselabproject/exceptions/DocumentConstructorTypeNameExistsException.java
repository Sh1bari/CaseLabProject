package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentConstructorTypeNameExistsException extends GlobalAppException {

    public DocumentConstructorTypeNameExistsException(String name) {
        super(422, "Document type " + name + " already exists.");
        log.warn(message);
    }
}
