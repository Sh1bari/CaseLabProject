package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserExistsException extends GlobalAppException {

    public UserExistsException(String username) {
        super(409, "User with username " + username + " already exists");
        log.warn(message);
    }
}
