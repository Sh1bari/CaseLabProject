package com.example.caselabproject.exceptions.department;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentCreateException extends GlobalAppException {

    public DepartmentCreateException() {
        super(500, "Can not create department!");
        log.warn(message);
    }
}