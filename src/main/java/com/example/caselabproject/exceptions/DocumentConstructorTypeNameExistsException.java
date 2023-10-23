package com.example.caselabproject.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class DocumentConstructorTypeNameExistsException extends RuntimeException {
    private int status;
    private String message;
    private Date timestamp;

    public DocumentConstructorTypeNameExistsException(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
