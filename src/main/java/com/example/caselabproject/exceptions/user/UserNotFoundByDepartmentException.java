package com.example.caselabproject.exceptions.user;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class UserNotFoundByDepartmentException extends GlobalAppException {
    public UserNotFoundByDepartmentException(Long userId, Long departmentId) {
        super(409, "Cant find user with id " + userId + " in department with id " + departmentId);
        log.warn(message);
    }
}
