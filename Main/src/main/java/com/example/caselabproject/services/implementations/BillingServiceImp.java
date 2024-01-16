package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.organization.OrganizationNotFoundException;
import com.example.caselabproject.exceptions.user.UserByPrincipalUsernameDoesNotExistException;
import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.repositories.BillRepository;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillingServiceImp {
    private final OrganizationRepository organizationRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public Map<Integer, Map<Month, List<BillingDaysAndPrice>>> calculationAllBilling(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));
        List<Bill> bills = billRepository.findAllByOrganization(organization);
        if (bills.isEmpty()) {
            return new HashMap<>();
        }
        Map<Integer, Map<Month, List<BillingDaysAndPrice>>> result = new TreeMap<>();
        Bill lastBill = bills.get(bills.size() - 1);
        LocalDateTime data = lastBill.getDate();
        int year = data.getYear();
        Month month = data.getMonth();
        Map<Month, List<BillingDaysAndPrice>> dataInYear = addBillingDaysAndPrice(month, lastBill.getDetails());
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
        }
        return result;
    }

    private BillingDaysAndPrice buildBillingDaysAndPrice(String subscription, Integer days, BigDecimal prices) {
        return BillingDaysAndPrice.builder()
                .days(days)
                .subscription(subscription)
                .prices(prices)
                .build();
    }

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
