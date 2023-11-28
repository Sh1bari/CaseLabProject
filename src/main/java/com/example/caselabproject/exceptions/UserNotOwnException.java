package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotOwnException extends GlobalAppException {

    public UserNotOwnException(String username) {
        super(403, "User " + username + " doesn't own application item");
        log.warn(message);
    }
}
