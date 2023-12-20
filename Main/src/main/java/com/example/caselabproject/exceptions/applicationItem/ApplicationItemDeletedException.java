package com.example.caselabproject.exceptions.applicationItem;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: Exception for deleted application item
 *
 * @author Vladimir Krasnov
 */
@Slf4j
public class ApplicationItemDeletedException extends GlobalAppException {
    public ApplicationItemDeletedException(Long applicationItemId) {
        super(409, "Application item with id " + applicationItemId + " has been deleted.");
    }
}
