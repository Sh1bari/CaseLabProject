package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationItemAlreadyHasBeenSentToUserException extends GlobalAppException {

    public ApplicationItemAlreadyHasBeenSentToUserException(Long userId) {
        super(409, "Application item already has been sent to user with id " + userId);
        log.warn(message);
    }
}
