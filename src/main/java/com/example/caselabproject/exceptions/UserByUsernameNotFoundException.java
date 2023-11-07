package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class UserByUsernameNotFoundException extends GlobalAppException{
    public UserByUsernameNotFoundException(String username) {
        super(404, "Cant find user with username: " + username);
        log.warn(message);
    }
}
