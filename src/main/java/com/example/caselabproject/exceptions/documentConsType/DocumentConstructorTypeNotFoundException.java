package com.example.caselabproject.exceptions.documentConsType;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Indicates, that a document type with a provided id isn't found.
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypeNotFoundException extends GlobalAppException {
    public DocumentConstructorTypeNotFoundException(Long id) {
        super(404, "Document type with id " + id + " isn't found");
        log.warn(message);
    }
}