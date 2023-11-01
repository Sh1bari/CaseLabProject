package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class DocumentConstructorTypeAlreadyDeletedException extends GlobalAppException{
    public DocumentConstructorTypeAlreadyDeletedException(Long id) {
        super(409, "Document type with id " + id + " already deleted.");
        log.warn(message);
    }
}
