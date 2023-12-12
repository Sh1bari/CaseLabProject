package com.example.caselabproject.services;

import com.example.caselabproject.models.enums.SubscriptionName;

import java.math.BigDecimal;
import java.util.EnumMap;

/**
 * Description: Service for generating pdf billing file
 *
 * @author Tribushko Danil
 */
public interface PdfFileService {
    void generatePdfBillingFile(EnumMap<SubscriptionName, Integer> usedDays,
                                    EnumMap<SubscriptionName, BigDecimal> prices,
                                    BigDecimal totalPrice);
}
