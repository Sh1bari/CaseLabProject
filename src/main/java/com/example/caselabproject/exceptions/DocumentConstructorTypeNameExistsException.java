package com.example.caselabproject.exceptions;

import lombok.Data;

import java.util.Date;

public class DocumentConstructorTypeNameExistsException extends GlobalAppException {

    public DocumentConstructorTypeNameExistsException(int status, String message) {
        super(status, message);
    }
}
