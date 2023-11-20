package com.example.caselabproject.exceptions.documentConsType;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Indicates, that a document type already has associated documents.
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypeHasAssociatedDocumentsException extends GlobalAppException {
    public DocumentConstructorTypeHasAssociatedDocumentsException(Long id) {
        super(HttpStatus.CONFLICT.value(), "DocumentConstructorType with id " + id + " has associated documents.");
        log.warn(message);
    }
}
