package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserIsNotDirectorException extends GlobalAppException {

    public UserIsNotDirectorException() {
        super(409, "The user you are sending to is not a director");
        log.warn(message);
    }
}