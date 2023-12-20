package com.example.caselabproject.exceptions.department;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentChildException extends GlobalAppException {

    public DepartmentChildException(Long childDep, Long parentDep) {
        super(409, "Department with id " + childDep + " is already a child of the department with id " + parentDep);
        log.warn(message);
    }
}
