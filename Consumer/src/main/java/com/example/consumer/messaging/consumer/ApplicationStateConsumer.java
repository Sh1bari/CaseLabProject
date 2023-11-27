package com.example.consumer.messaging.consumer;

public interface ApplicationStateConsumer {
    void handle(String applicationData);
}
