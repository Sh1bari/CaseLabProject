package com.example.caselabproject.exceptions.application;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: Exception for deleted application
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationDeletedException extends GlobalAppException {
    public ApplicationDeletedException(Long applicationId) {
        super(409, "Application with id " + applicationId + " has been deleted.");
        log.warn(message);
    }
}
