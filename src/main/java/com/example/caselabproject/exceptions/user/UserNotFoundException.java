package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends GlobalAppException {

    public UserNotFoundException(Long id) {
        super(404, "User with id " + id + " not found.");
        log.warn(message);
    }

    public UserNotFoundException(String username) {
        super(404, "User with username " + username + " not found.");
        log.warn(message);
    }
}
