package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlienOrganizationException extends GlobalAppException{
    public AlienOrganizationException(String userName, String organizationName) {
        super(400, "user " + userName + " is trying to change not his organization " + organizationName);
        log.warn("400: user " + userName + " is trying to change not his organization " + organizationName);
    }
}
