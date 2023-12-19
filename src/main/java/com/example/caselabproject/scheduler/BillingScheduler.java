package com.example.caselabproject.scheduler;


import com.example.caselabproject.models.entities.Bill;
import com.example.caselabproject.models.entities.BillingLog;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import com.example.caselabproject.repositories.BillRepository;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.services.PdfFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;


@Component
@RequiredArgsConstructor
public class BillingScheduler {
    
    private final PdfFileService pdfFileService;
    private final OrganizationRepository organizationRepository;
    private final BillingLogRepository billingLogRepository;
    private final BillRepository billRepository;
    
    @Scheduled(cron = "0 0 1 1 * *") // 01:00 on the first day of every month
    public void setScheduler() {
        for (Organization organization : organizationRepository.findAll()) {
            processBillingLog(
                    organization,
                    billingLogRepository.findAllByIdAndSubscriptionStartBetweenOrderBySubscriptionStart(
                            organization.getId(), LocalDateTime.now().minusMonths(1), LocalDateTime.now())
            );
        }
    }
    
    private void processBillingLog(Organization organization, List<BillingLog> billingLog) {
        final Subscription current = organization.getSubscription();
        final int daysPerLastMonth = getDaysPerLastMonth();
        
        BigDecimal total = getScaledBigDecimal(0);
        Map<Subscription, Integer> usages = new HashMap<>();
        Map<Subscription, BigDecimal> prices = new HashMap<>();
        
        int daysToRemove = 0;
        for (BillingLog entry : billingLog) {
            Subscription before = entry.getLastSubscription();
            var usage = entry.getSubscriptionStart().getDayOfMonth() - daysToRemove;
            var price = getScaledBigDecimal(before.getCost() / daysPerLastMonth)
                    .multiply(valueOf(usage));
            
            daysToRemove += usage;
            
            total = total.add(price);
            usages.merge(before, usage, Integer::sum);
            prices.merge(before, price, BigDecimal::add);
        }
        
        var usage = daysPerLastMonth - daysToRemove;
        var price = getScaledBigDecimal(current.getCost() / daysPerLastMonth)
                .multiply(valueOf(usage));
        
        total = total.add(price);
        usages.merge(current, usage, Integer::sum);
        prices.merge(current, price, BigDecimal::add);
        
        billRepository.save(buildBill(organization, total, usages));

//        pdf generation - question (only 3.1)
//        pdfFileService.generatePdfBillingFile(usages, prices, total);
    }
    
    private int getDaysPerLastMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return YearMonth
                .of(lastMonth.getYear(), lastMonth.getMonth())
                .lengthOfMonth();
    }
    
    private BigDecimal getScaledBigDecimal(double number) {
        return valueOf(number).setScale(8, RoundingMode.UP);
    }
    
    private Bill buildBill(Organization organization, BigDecimal total,
                           Map<Subscription, Integer> usages) {
        return Bill.builder()
                .organization(organization)
                .date(LocalDateTime.now())
                .total(total.floatValue())
                .details(usages)
                .build();
    }
}
