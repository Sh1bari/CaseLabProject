package com.example.caselabproject.exceptions;

public class UserByPrincipalUsernameDoesNotExistException extends GlobalAppException{
    public UserByPrincipalUsernameDoesNotExistException(String username) {
        super(500, "Server can not find user with username " + username);
    }
}
