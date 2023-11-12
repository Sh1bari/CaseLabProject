package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentDoesNotExistException extends GlobalAppException {

    public DocumentDoesNotExistException(Long id) {
        super(404, "Document with id:" + id + " does not exist!");
        log.warn(message);
    }
}