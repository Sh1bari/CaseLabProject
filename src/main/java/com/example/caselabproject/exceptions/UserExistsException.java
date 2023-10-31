package com.example.caselabproject.exceptions;

public class UserExistsException extends GlobalAppException {

    public UserExistsException(String username) {
        super(409, "User with username " + username + " already exists");
    }
}
