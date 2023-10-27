package com.example.caselabproject.exceptions;


public class DocumentCreateException extends GlobalAppException {
    public DocumentCreateException() {
        super(500, "Could not create document!");
    }
}
