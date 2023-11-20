package com.example.caselabproject.exceptions.documentConsType;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Indicates, that a document type was already deleted
 * (can't be changed to {@link com.example.caselabproject.models.enums.RecordState#DELETED} again).
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypeAlreadyDeletedException extends GlobalAppException {
    public DocumentConstructorTypeAlreadyDeletedException(Long id) {
        super(409, "Document type with id " + id + " already deleted.");
        log.warn(message);
    }
}
