package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class UserDeletedException extends GlobalAppException{
    public UserDeletedException(Long userId) {
        super(409, "User with id " + userId + " has been deleted.");
    }
}
