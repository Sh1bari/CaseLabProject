package com.example.caselabproject;

import com.example.caselabproject.messaging.producer.ApplicationStateProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaseLabProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseLabProjectApplication.class, args);
    }

    @Bean
    public Object test(@Autowired ApplicationStateProducer kafkaTemplate) {
        kafkaTemplate.sendApplication("hellox1434311");
        return new Object();
    }
}
