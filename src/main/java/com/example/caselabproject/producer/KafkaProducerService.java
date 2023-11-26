package com.example.caselabproject.producer;

import com.example.caselabproject.models.entities.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendApplication(Application application) {
        kafkaTemplate.send("application-topic", application.getName());
    }
}
