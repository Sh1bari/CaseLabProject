package com.example.caselabproject.messaging.producer;


import java.math.BigDecimal;


public interface BillingTotalByMonthProducer {
    void send(Long organizationId, BigDecimal total, Long billId);
}
