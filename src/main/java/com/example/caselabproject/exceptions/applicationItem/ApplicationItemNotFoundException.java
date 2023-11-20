package com.example.caselabproject.exceptions.applicationItem;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemNotFoundException extends GlobalAppException {
    public ApplicationItemNotFoundException(Long id) {
        super(404, "Application item with id " + id + " not found");
        log.warn(message);
    }
}
