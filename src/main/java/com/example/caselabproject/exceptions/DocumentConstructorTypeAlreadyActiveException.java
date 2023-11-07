package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Indicates, that a document type was already activated
 * (can't be changed to {@link com.example.caselabproject.models.enums.RecordState#ACTIVE} again).
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypeAlreadyActiveException extends GlobalAppException {
    public DocumentConstructorTypeAlreadyActiveException(Long id) {
        super(409, "Document type with id " + id + " already active.");
        log.warn(message);
    }
}
