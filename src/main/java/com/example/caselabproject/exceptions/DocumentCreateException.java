package com.example.caselabproject.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class DocumentCreateException extends RuntimeException {
    private int status;
    private String message;
    private Date timestamp;

    public DocumentCreateException(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
