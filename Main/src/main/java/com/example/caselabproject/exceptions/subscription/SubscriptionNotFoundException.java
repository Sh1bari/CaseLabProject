package com.example.caselabproject.exceptions.subscription;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriptionNotFoundException extends GlobalAppException {
    public SubscriptionNotFoundException(Long id) {
        super(404, "Subscription with id " + id + " not found.");
        log.warn(message);
    }
}
