package com.example.caselabproject.scheduler;


import com.example.caselabproject.models.entities.BillingLog;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BillingScheduler {
    
    private final OrganizationRepository organizationRepository;
    private final BillingLogRepository billingLogRepository;
    private final SubscriptionRepository subscriptionRepository;
    
    @Transactional
    @Scheduled(cron = "0 0 0 1 * *") // 00:00 on the first day of every month
    public void setScheduler() {
        for (Organization organization : organizationRepository.findAll()) {
            processBillingLog(organization,
                    billingLogRepository.findAllByIdAndSubscriptionStartBetweenOrderBySubscriptionStart(
                            organization.getId(), LocalDateTime.now().minusMonths(1), LocalDateTime.now()));
        }
    }
    
    private void processBillingLog(Organization organization, Optional<List<BillingLog>> optionalBillingLog) {
//        final Subscription current = organization.getSubscription();
//        final int daysPerMonth = YearMonth
//                .of(LocalDate.now().getYear(), LocalDate.now().getMonth())
//                .lengthOfMonth();
//
//        EnumMap<SubscriptionName, Integer> usedDays = new EnumMap<>(SubscriptionName.class);
//        EnumMap<SubscriptionName, BigDecimal> prices = new EnumMap<>(SubscriptionName.class);
//        BigDecimal total = BigDecimal.ZERO;
//
//        if (optionalBillingLog.isPresent()) {
//            List<BillingLog> billingLog = optionalBillingLog.get();
//            for (int i = 0; i < billingLog.size(); i++) {
//                if (i == 0) {
//                    BillingLog first = billingLog.get(0);
//                    Subscription before = first.getLastSubscription();
//                    BigDecimal costPerDay = BigDecimal.valueOf(before.getCost() / daysPerMonth);
//                    total = costPerDay.multiply(BigDecimal.valueOf(first.getSubscriptionStart().getDayOfMonth()));
//                } else if (i == billingLog.size() - 1) {
//                    BillingLog last = billingLog.get(i);
//                    BigDecimal costPerDay = BigDecimal.valueOf(current.getCost() / daysPerMonth);
//                    total = costPerDay.multiply(BigDecimal.valueOf(daysPerMonth - billingLog.get(i).getSubscriptionStart().getDayOfMonth()));
//                } else {
//                    BillingLog log = billingLog.get(i);
//                    BigDecimal costPerDay = BigDecimal.
//                }
//            }
//        } else {
//            total = BigDecimal.valueOf(current.getCost());
//            prices.put(current.getSubscriptionName(), total);
//            usedDays.put(current.getSubscriptionName(), daysPerMonth);
//        }
//
////        someService.send(usedDays, prices, total);
//
//        // Даня
////        Генерируем файл -счет и кладем в кафку в отдельный топик
////        ссылку на этот файл в minio.
////        Консьюмер будет иметь технического пользователя, который сможет читать эти счета
////        из специального бакета в minio.
////                Формат файла - pdf:
////        Тариф Х -10 дней
////        Тариф Y -20 дней
////        Итоговая стоимость месяца
    }
}
