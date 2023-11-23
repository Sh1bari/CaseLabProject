package com.example.caselabproject.exceptions.department;


import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentNameExistsException extends GlobalAppException {

    public DepartmentNameExistsException(String name) {
        super(422, "Department " + name + " already exists.");
        log.warn(message);
    }
}