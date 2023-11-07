package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemNotFoundException extends GlobalAppException{
    public ApplicationItemNotFoundException(Long id) {
        super(404, "Application item with id " + id + " not found");
        log.warn(message);
    }
}
