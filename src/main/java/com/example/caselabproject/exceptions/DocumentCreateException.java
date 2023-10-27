package com.example.caselabproject.exceptions;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentCreateException extends GlobalAppException {
    public DocumentCreateException(String name) {
        super(500, "Can not create document " + name);
        log.warn(message);
    }
}
