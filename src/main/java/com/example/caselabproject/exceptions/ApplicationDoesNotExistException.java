package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationDoesNotExistException extends GlobalAppException{
    public ApplicationDoesNotExistException(Long id) {
        super(404, "Application with id " + id + " does not exist!");
        log.warn(message);
    }
}
