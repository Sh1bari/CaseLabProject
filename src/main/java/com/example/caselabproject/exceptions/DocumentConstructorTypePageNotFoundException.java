package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentConstructorTypePageNotFoundException extends GlobalAppException{
    public DocumentConstructorTypePageNotFoundException(Integer page) {
        super(404, "Page with number "+ page +" not found");
        log.warn(message);
    }
}
