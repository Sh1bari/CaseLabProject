package com.example.caselabproject;

//import com.example.caselabproject.messaging.producer.ApplicationStateProducer;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class CaseLabProjectApplication {

    private final KafkaProducerService kafkaProducerService;

    public static void main(String[] args) {
        SpringApplication.run(CaseLabProjectApplication.class, args);
    }


//    @Bean
//    public Object test(@Autowired ApplicationStateProducer kafkaTemplate) {
//        kafkaTemplate.sendApplication("hellox1434311");
//        return new Object();
//    }
}
