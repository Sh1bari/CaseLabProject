package com.example.caselabproject.exceptions.department;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentChildParentException extends GlobalAppException {

    public DepartmentChildParentException() {
        super(500, "You can't tie a department to yourself");
        log.warn(message);
    }

    public DepartmentChildParentException(Long childId, Long parentId) {
        super(409, "You cannot make a department " + childId + " parent on " + parentId + " because " + parentId + " is already parent of " + childId);
        log.warn(message);
    }
}
