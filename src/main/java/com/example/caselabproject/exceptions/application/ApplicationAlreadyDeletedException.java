package com.example.caselabproject.exceptions.application;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationAlreadyDeletedException extends GlobalAppException {
    public ApplicationAlreadyDeletedException(Long id) {
        super(409, "Application with id " + id + " already deleted.");
        log.warn(message);
    }
}
