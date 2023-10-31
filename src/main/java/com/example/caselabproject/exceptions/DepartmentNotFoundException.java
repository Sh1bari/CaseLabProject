package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class DepartmentNotFoundException extends GlobalAppException {

    public DepartmentNotFoundException(Long departmentId) {
        super(404, "Department not found with id " + departmentId);
        log.warn(message);
    }
}
