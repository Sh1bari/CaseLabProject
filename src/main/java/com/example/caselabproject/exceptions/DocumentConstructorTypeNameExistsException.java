package com.example.caselabproject.exceptions;



public class DocumentConstructorTypeNameExistsException extends GlobalAppException {

    public DocumentConstructorTypeNameExistsException(int status, String message) {
        super(status, message);
    }
}
