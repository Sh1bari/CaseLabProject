package com.example.caselabproject.exceptions;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentStatusException extends GlobalAppException {

    public DepartmentStatusException(Long departmentId) {
        super(409, "Department " + departmentId + " already has the status DELETED");
        log.warn(message);
    }
}