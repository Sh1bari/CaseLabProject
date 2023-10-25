package com.example.caselabproject.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DocumentTypeIdNotExistsException extends RuntimeException {
    private final int status;
    private final String message;
    private final LocalDateTime dateTime;

    public DocumentTypeIdNotExistsException(int status, String message) {
        this.status = status;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}