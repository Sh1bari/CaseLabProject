package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotCreatorException extends GlobalAppException {
    public UserNotCreatorException(String username) {
        super(403, "User " + username + " is not creator");
        log.warn(message);
    }
}
