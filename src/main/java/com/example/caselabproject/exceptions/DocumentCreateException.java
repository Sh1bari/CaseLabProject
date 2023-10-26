package com.example.caselabproject.exceptions;


public class DocumentCreateException extends GlobalAppException {
    public DocumentCreateException(int status, String message) {
        super(status, message);
    }
}
