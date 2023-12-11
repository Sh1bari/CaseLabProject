package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;

public class DirectorIsNotLastException extends GlobalAppException {

    public DirectorIsNotLastException(long departmentId, long directorId) {
        super(403, "In department " + departmentId + " director with id " + directorId + " is not last active user");
    }
}
