package com.example.caselabproject.services;


import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.enums.SubscriptionName;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
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
    void generatePdfBillingDetailsFile(Map<Integer, Map<Month, List<BillingDaysAndPrice>>> details);
}
