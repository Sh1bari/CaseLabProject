package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class DepartmentDeletedException extends GlobalAppException {
    public DepartmentDeletedException(Long departmentId) {
        super(409, "Department with id " + departmentId + " has been deleted.");
    }
}
