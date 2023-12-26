package com.example.caselabproject.scheduler;


import com.example.caselabproject.messaging.producer.BillingTotalByMonthProducer;
import com.example.caselabproject.messaging.producer.implementation.KafkaBillingTotalByMonthProducer;
import com.example.caselabproject.models.entities.Bill;
import com.example.caselabproject.models.entities.BillingLog;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import com.example.caselabproject.repositories.BillRepository;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
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

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;


@Component
@RequiredArgsConstructor
public class BillingScheduler {
    
    private final OrganizationRepository organizationRepository;
    private final BillingLogRepository billingLogRepository;
    private final BillRepository billRepository;
    
    private final BillingTotalByMonthProducer billingProducer;
    
    @Scheduled(cron = "0 0 1 1 * *") // 01:00 on the first day of every month
    public void start() {
        for (Organization organization : organizationRepository.findAll()) {
            List<BillingLog> billingLog = billingLogRepository
                    .findAllByIdAndSubscriptionStartBetweenOrderBySubscriptionStart(
                            organization.getId(), LocalDateTime.now().minusMonths(1), LocalDateTime.now());
            
            processBillingLog(organization, billingLog);
        }
    }
    
    private void processBillingLog(Organization organization, List<BillingLog> billingLog) {
        final int daysPerLastMonth = getDaysPerLastMonth();

//        accumulative variables
        BigDecimal total = ZERO;
        Map<Subscription, Integer> usages = new HashMap<>();
        Map<Subscription, BigDecimal> prices = new HashMap<>();
        
        /*
        If there are no history entries from `billingLog`,
            calculate the price for current organization's subscription anyway.
        That's why here's `billingLog.size() + 1`.
        */
        int alreadyUsedDays = 0;
        for (int i = 0; i < billingLog.size() + 1; i++) {
            Subscription subscription;
            int usage;
            
            if (i < billingLog.size()) {
                subscription = billingLog.get(i).getLastSubscription();
                usage = billingLog.get(i).getSubscriptionStart().getDayOfMonth() - alreadyUsedDays;
            } else {
                subscription = organization.getSubscription();
                usage = daysPerLastMonth - alreadyUsedDays;
            }
            
            BigDecimal price = getScaledBigDecimal(subscription.getCost() / daysPerLastMonth)
                    .multiply(valueOf(usage));
            alreadyUsedDays += usage;
            
            total = total.add(price);
            usages.merge(subscription, usage, Integer::sum);
            prices.merge(subscription, price, BigDecimal::add);
        }
        
        Bill monthlyBill = billRepository.save(buildBill(organization, total, usages));
        billingProducer.send(organization.getId(), total, monthlyBill.getId());
        // make Запрос информации из reddiss
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
    
    private final KafkaBillingTotalByMonthProducer kafkaBillingTotalByMonthProducer;
    
    //@Scheduled(cron = "*/1 * * * * *")
    /*public void test() {
        kafkaBillingTotalByMonthProducer
                .send(1L, BigDecimal.valueOf(4343).multiply(BigDecimal.valueOf(Math.random())), 4L);
    }*/
}
