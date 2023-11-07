package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationAlreadyDeletedException extends GlobalAppException{
    public ApplicationAlreadyDeletedException(Long id) {
        super(409, "Application with id " + id + " already deleted.");
        log.warn(message);
    }
}
