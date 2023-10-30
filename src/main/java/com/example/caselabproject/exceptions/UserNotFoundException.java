package com.example.caselabproject.exceptions;

public class UserNotFoundException extends GlobalAppException {

    public UserNotFoundException(Long id) {
        super(404, "User with id " + id + " not found");
    }
}
