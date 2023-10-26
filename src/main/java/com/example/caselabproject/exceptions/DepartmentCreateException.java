package com.example.caselabproject.exceptions;


public class DepartmentCreateException extends GlobalAppException {

    public DepartmentCreateException(int status, String message) {
        super(status, message);
    }
}