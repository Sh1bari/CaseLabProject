package com.example.caselabproject.exceptions;

public class DocumentDoesNotExistException extends GlobalAppException {

    public DocumentDoesNotExistException(Long id) {
        super(404, "Document with id:" + id + " does not exist!");
    }
}
