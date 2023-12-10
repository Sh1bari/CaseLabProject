package com.example.caselabproject.scheduler;


import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BillingScheduler {
    
    private final OrganizationRepository organizationRepository;
    private final BillingLogRepository billingLogRepository;
    private final SubscriptionRepository subscriptionRepository;
    //3.2 Нужен отдельный шедулер, который каждое 1 число месяца
    // считает стоимость предыдущего для каждой организации в зависимости
    // от ее тарифа
    
    @Scheduled(cron = "@monthly")
    public void setScheduler() {
//        final int daysPerMonth = YearMonth
//                .of(LocalDate.now().getYear(), LocalDate.now().getMonth())
//                .lengthOfMonth();
//        final double pricePerBasic = subscriptionRepository
//                .findCostBySubscriptionName(SubscriptionName.BASIC);
//        final double pricePerStandard = subscriptionRepository
//                .findCostBySubscriptionName(SubscriptionName.STANDARD);
//        final double pricePerEnterprise = subscriptionRepository
//                .findCostBySubscriptionName(SubscriptionName.ENTERPRISE);
//
//        Map<SubscriptionName, Integer> subscriptionAndDays = new HashMap<>(Map.of(
//                SubscriptionName.DEFAULT, 0,
//                SubscriptionName.BASIC, 0,
//                SubscriptionName.STANDARD, 0,
//                SubscriptionName.ENTERPRISE, 0));
//
//        BigDecimal price = BigDecimal.ZERO;
//
//        List<Organization> organizations = organizationRepository.findAll();
//        for (Organization organization : organizations) {
//            Optional<List<BillingLog>> billingLog =
//                    billingLogRepository.findAllByIdAndSubscriptionStartBetweenOrderBySubscriptionStart(
//                            organization.getId(), LocalDateTime.now().minusMonths(1), LocalDateTime.now());
//
//            if (billingLog.isPresent()) {
//                int daysToRemove = 0;
//                List<BillingLog> log = billingLog.get();
//
//                BillingLog piece = log.remove(0);
//                subscriptionAndDays.merge(piece.getCurrentSubscription().getSubscriptionName(), );
//
////                billingLog.get().forEach((record) ->
////                        subscriptionAndDays.compute()
////                );
//            } else {
//                price = price.add()
//            }
//        }

//        Генерируем файл-счет и кладем в кафку в отдельный топик
//          ссылку на этот файл в minio.
//        Консьюмер будет иметь технического пользователя, который сможет читать эти счета
//          из специального бакета в minio.
//        Формат файла - pdf:
//          Тариф Х - 10 дней
//          Тариф Y - 20 дней
//          Итоговая стоимость месяца
    }
}
