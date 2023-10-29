package com.example.caselabproject.exceptions;

public class DocumentAccessException extends GlobalAppException {

    public DocumentAccessException(String username) {
        super(499, "User " + username + " does not have access to this document!");
    }
}
