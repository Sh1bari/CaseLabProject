package com.example.caselabproject.exceptions.department;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentParentException extends GlobalAppException {

    public DepartmentParentException() {
        super(409, "Department has parent department.");
        log.warn(message);
    }
}
