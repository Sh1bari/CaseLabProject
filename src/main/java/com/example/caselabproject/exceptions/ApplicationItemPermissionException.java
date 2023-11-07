package com.example.caselabproject.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemPermissionException extends GlobalAppException{
    public ApplicationItemPermissionException(Long id) {
        super(403, "You don't have enough rights for access to Application item with id " + id);
        log.warn(message);
    }
    public ApplicationItemPermissionException() {
        super(403, "You don't have enough rights for access to Application items");
        log.warn(message);
    }
}
