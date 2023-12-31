package com.example.caselabproject.exceptions.applicationItem;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemAlreadyTakenException extends GlobalAppException {
    public ApplicationItemAlreadyTakenException(Long applicationItemId) {
        super(409, "Application item with id " + applicationItemId + " has been already taken.");
    }
}
