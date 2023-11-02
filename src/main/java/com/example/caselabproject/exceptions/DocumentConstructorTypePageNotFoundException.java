package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Indicates, that a page with a provided name isn't found.
 *
 * @see com.example.caselabproject.models.entities.DocumentConstructorType
 */
@Slf4j
public class DocumentConstructorTypePageNotFoundException extends GlobalAppException {
    public DocumentConstructorTypePageNotFoundException(Integer page) {
        super(404, "Page with number " + page + " not found");
        log.warn(message);
    }
}
