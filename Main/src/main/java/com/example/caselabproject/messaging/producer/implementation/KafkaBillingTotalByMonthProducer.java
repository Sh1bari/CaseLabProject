package com.example.caselabproject.messaging.producer.implementation;


import com.example.caselabproject.messaging.producer.BillingTotalByMonthProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class KafkaBillingTotalByMonthProducer implements BillingTotalByMonthProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Override
    public void send(Long organizationId, BigDecimal total, Long billId) {
        kafkaTemplate.send("billing-topic",
                "Organization (id: %d) has total monthly price: %s. More information by Bill id: %d"
                        .formatted(organizationId, total, billId));
    }
}
