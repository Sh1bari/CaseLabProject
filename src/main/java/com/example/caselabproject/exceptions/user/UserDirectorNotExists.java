package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDirectorNotExists extends GlobalAppException {

    public UserDirectorNotExists(Long departmentId) {
        super(404, "The department with" + departmentId + " does not have a director; you cannot send ApplicationItem");
        log.warn(message);
    }
}