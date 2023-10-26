package com.example.caselabproject.exceptions;


public class DepartmentNotFoundException extends GlobalAppException {

    public DepartmentNotFoundException(int status, String message) {
        super(status, message);
    }
}