package com.example.caselabproject.exceptions;




public class DepartmentNameExistsException  extends GlobalAppException {

    public DepartmentNameExistsException(int status, String message) {
        super(status, message);
    }
}