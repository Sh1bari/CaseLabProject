package com.example.caselabproject.exceptions.subscription;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriptionShouldChangeException extends GlobalAppException {

    public SubscriptionShouldChangeException(Integer users) {
        super(409, "You should change Subscription, because the current subscription does not allow you to have more users than" + users);
        log.warn(message);
    }
}
