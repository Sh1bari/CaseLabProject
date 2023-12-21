package com.example.caselabproject.messaging.producer;

import com.example.caselabproject.models.entities.Application;

public interface ApplicationStateProducer {
    void send(Application application);
}
