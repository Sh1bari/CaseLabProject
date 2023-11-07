package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemAlreadyHasBeenSentToDepartmentException extends GlobalAppException{
    public ApplicationItemAlreadyHasBeenSentToDepartmentException(Long departmentId) {
        super(409, "Application item already has been sent to department with id " + departmentId);
        log.warn(message);
    }
}
