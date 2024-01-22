package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.organization.OrganizationNotFoundException;
import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationIdRequestDto;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.repositories.BillRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillingServiceImp implements BillingService {
    private final OrganizationRepository organizationRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    @Override
    public Map<Integer, Map<Month, List<BillingDaysAndPrice>>> calculationAllBilling(OrganizationIdRequestDto request) {
        Organization organization = organizationRepository.findById(request.getId())
                .orElseThrow(() -> new OrganizationNotFoundException(request.getId()));
        List<Bill> bills = billRepository.findAllByOrganization(organization);
        if (bills.isEmpty()) {
            return new HashMap<>();
        }
        Map<Integer, Map<Month, List<BillingDaysAndPrice>>> result = new TreeMap<>();
        Bill lastBill = bills.get(bills.size() - 1);
        //Получаем последний билл
        LocalDateTime date = lastBill.getDate();
        int year = date.getYear();
        Month month = date.getMonth();
        //Добавляем данные за месяц
        Map<Month, List<BillingDaysAndPrice>> dataInYear = addBillingDaysAndPrice(month, lastBill.getDetails());
        if (bills.size() == 1){
            result.put(year, dataInYear);
        }
        for (int j = bills.size() - 2; j > -1; j--) {
            Bill bill = bills.get(j);
            LocalDateTime dataBill = bill.getDate();
            int yearBill = dataBill.getYear();
            month = dataBill.getMonth();
            if (year == yearBill){
                dataInYear.putAll(addBillingDaysAndPrice(month, bill.getDetails()));
            } else {
                result.put(year, dataInYear);
                dataInYear = addBillingDaysAndPrice(month, bill.getDetails());
                year = yearBill;
            }
            if (j == 0){
                result.put(year, dataInYear);
            }
        }
        return result;
    }

    /**
     * Получение количество дней и цену для подписки
     * @param subscription подписка
     * @param days количество дней пользования
     * @param prices цена
     */
    private BillingDaysAndPrice buildBillingDaysAndPrice(String subscription, Integer days, BigDecimal prices) {
        return BillingDaysAndPrice.builder()
                .days(days)
                .subscription(subscription)
                .prices(prices)
                .build();
    }

    /**
     * Добавление количество дней и цену за все подписки с определенным месяцем в мапу
     * @param month месяц
     * @param details детализация по билу
     */
    private Map<Month, List<BillingDaysAndPrice>> addBillingDaysAndPrice(Month month,
                                                                         Map<Subscription, Integer> details) {
        Map<Month, List<BillingDaysAndPrice>> dataInYear = new TreeMap<>();
        List<BillingDaysAndPrice> billingDaysAndPrices = new ArrayList<>();
        for (Map.Entry<Subscription, Integer> map : details.entrySet()) {
            BigDecimal prices = BigDecimal.valueOf(map.getKey().getCost() / map.getValue())
                    .setScale(8, RoundingMode.UP);
            billingDaysAndPrices.add(buildBillingDaysAndPrice(map.getKey().getSubscriptionName(),
                    map.getValue(),
                    prices));
        }
        dataInYear.put(month, billingDaysAndPrices);
        return dataInYear;
    }
}
