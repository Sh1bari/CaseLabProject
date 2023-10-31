package com.example.caselabproject.exceptions;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentCreateException extends GlobalAppException {
    public DocumentCreateException(int status, String message) {
        super(status, message);
        log.warn(message);
    }
}
