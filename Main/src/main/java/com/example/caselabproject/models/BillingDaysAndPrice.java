package com.example.caselabproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingDaysAndPrice {
    private String subscription;
    private Integer days;
    private BigDecimal prices;
}
