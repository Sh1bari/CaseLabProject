package com.example.caselabproject.exceptions.application;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationNotFoundException extends GlobalAppException {
    public ApplicationNotFoundException(Long id) {
        super(404, "Application with id " + id + " not found");
        log.warn(message);
    }
}
