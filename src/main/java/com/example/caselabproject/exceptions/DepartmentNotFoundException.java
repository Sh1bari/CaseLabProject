package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentNotFoundException extends GlobalAppException {

    public DepartmentNotFoundException(Long id) {
        super(404, "Department not found with id: " + id);
        log.warn(message);
    }
}