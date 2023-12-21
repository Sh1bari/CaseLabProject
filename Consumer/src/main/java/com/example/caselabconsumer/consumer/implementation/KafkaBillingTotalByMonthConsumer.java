package com.example.caselabconsumer.consumer.implementation;


import com.example.caselabconsumer.consumer.BillingTotalByMonthConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaBillingTotalByMonthConsumer implements BillingTotalByMonthConsumer {
    
    @Override
    @KafkaListener(topics = "billing-topic", groupId = "billing")
    public void handle(String data) {
        log.info("Accepted billing check: {}", data);
    }
}
