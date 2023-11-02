package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Indicates, that {@link com.example.caselabproject.models.entities.DocumentConstructorType}
 * has associated documents.
 */
@Slf4j
public class DocumentConstructorTypeHasAssociatedDocumentsException extends GlobalAppException {

    public DocumentConstructorTypeHasAssociatedDocumentsException(Long id) {
        super(HttpStatus.CONFLICT.value(), "DocumentConstructorType with id " + id + " has associated documents.");
        log.warn(message);
    }
}
