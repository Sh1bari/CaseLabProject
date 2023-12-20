package com.example.caselabproject.exceptions.documentConsType;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Indicates, that a document type with a provided name already exists.
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypeNameExistsException extends GlobalAppException {
    public DocumentConstructorTypeNameExistsException(String name) {
        super(422, "Document type " + name + " already exists.");
        log.warn(message);
    }
}
