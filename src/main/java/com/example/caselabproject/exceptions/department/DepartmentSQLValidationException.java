package com.example.caselabproject.exceptions.department;


import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentSQLValidationException extends GlobalAppException {

    public DepartmentSQLValidationException(String name) {
        super(422, "Department " + name + "  SQL Validation exception.");
        log.warn(message);
    }
}