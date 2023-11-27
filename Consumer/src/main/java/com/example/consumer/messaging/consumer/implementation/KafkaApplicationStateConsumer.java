package com.example.consumer.messaging.consumer.implementation;

import com.example.consumer.messaging.consumer.ApplicationStateConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaApplicationStateConsumer implements ApplicationStateConsumer {
    @KafkaListener(topics = "application-topic", groupId = "application")
    @Override
    public void handle(String applicationData) {
        log.info("Accepted application: {}", applicationData);
    }
}
