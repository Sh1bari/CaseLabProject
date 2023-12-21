package com.example.caselabconsumer.consumer.implementation;


import com.example.caselabconsumer.consumer.ApplicationStateConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaApplicationStateConsumer implements ApplicationStateConsumer {
    
    @Override
    @KafkaListener(topics = "application-topic", groupId = "application")
    public void handle(String applicationData) {
        log.info("Accepted application: {}", applicationData);
    }
}
