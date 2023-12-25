package com.example.caselabproject.exceptions.organization;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrganizationNotFoundException extends GlobalAppException {

    public OrganizationNotFoundException(Long id) {
        super(404, "Organization with id " + id + " not found.");
        log.warn(message);
    }
}
