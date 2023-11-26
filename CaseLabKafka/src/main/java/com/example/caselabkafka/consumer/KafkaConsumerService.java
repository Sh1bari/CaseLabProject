package com.example.caselabkafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "application-topic")
    public void listener(String message) {
        System.out.println("Accepted application: " + message);
    }
}
