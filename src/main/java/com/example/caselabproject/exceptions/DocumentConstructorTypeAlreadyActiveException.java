package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class DocumentConstructorTypeAlreadyActiveException extends GlobalAppException{
    public DocumentConstructorTypeAlreadyActiveException(Long id) {
        super(409, "Document type with id " + id + " already active.");
        log.warn(message);
    }
}
