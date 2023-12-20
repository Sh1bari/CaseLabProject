package com.example.caselabproject.messaging.producer.implementation;

import com.example.caselabproject.messaging.producer.ApplicationStateProducer;
import com.example.caselabproject.models.entities.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaApplicationStateProducer implements ApplicationStateProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(Application application) {
        kafkaTemplate.send("application-topic", application.getName());
    }
}
