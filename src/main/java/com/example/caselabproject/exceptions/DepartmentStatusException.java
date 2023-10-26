package com.example.caselabproject.exceptions;




public class DepartmentStatusException extends GlobalAppException {

    public DepartmentStatusException(int status, String message) {
        super(status, message);
    }
}