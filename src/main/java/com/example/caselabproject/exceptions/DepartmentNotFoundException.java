package com.example.caselabproject.exceptions;


public class DepartmentNotFoundException extends GlobalAppException {

    public DepartmentNotFoundException(Long id) {
        super(404, "Department not found with id: " + id);
    }
}