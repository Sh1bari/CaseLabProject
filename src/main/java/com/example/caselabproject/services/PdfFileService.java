package com.example.caselabproject.services;


import com.example.caselabproject.models.enums.SubscriptionName;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Description: Service for generating pdf billing file
 *
 * @author Tribushko Danil
 */
public interface PdfFileService {
    void generatePdfBillingFile(Map<SubscriptionName, Integer> usages,
                                Map<SubscriptionName, BigDecimal> prices,
                                BigDecimal total);
}
