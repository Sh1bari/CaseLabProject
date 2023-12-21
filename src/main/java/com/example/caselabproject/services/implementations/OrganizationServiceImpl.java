package com.example.caselabproject.services.implementations;

import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;
import com.example.caselabproject.models.entities.BillingLog;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import com.example.caselabproject.models.enums.SubscriptionName;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.SubscriptionRepository;
import com.example.caselabproject.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final SubscriptionRepository subscriptionRepository;
    private final BillingLogRepository billingLogRepository;


    @Override
    // прередавать id подписки
    // id орг через принципал
    // проверка количество людей из организации и доступ к более низкому тарифу
    public OrganizationSubscriptionChangeResponseDto raiseSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto) {
        Organization organization = organizationRepository.getReferenceById(subscriptionChangeRequestDto.getId());
        Subscription currentSubscription = organization.getSubscription();
        BillingLog billingLog = new BillingLog();

        switch (currentSubscription.getSubscriptionName()) {
            case DEFAULT -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.BASIC);
            case BASIC -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.STANDARD);
            case STANDARD ->
                    setSubscription(billingLog, currentSubscription, organization, SubscriptionName.ENTERPRISE);
        }

        return OrganizationSubscriptionChangeResponseDto.mapFromEntity(organization);
    }

    @Override
    public OrganizationSubscriptionChangeResponseDto lowerSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto) {
        Organization organization = organizationRepository.getReferenceById(subscriptionChangeRequestDto.getId());
        Subscription currentSubscription = organization.getSubscription();
        BillingLog billingLog = new BillingLog();

        switch (currentSubscription.getSubscriptionName()) {
            case ENTERPRISE ->
                    setSubscription(billingLog, currentSubscription, organization, SubscriptionName.STANDARD);
            case STANDARD -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.BASIC);
            case BASIC -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.DEFAULT);
        }

        return OrganizationSubscriptionChangeResponseDto.mapFromEntity(organization);
    }



    private void setSubscription(BillingLog billingLog, Subscription currentSubscription, Organization organization, SubscriptionName subscriptionName) {
        billingLog.setLastSubscription(currentSubscription);

        currentSubscription.setSubscriptionName(subscriptionName);

        billingLog.setCurrentSubscription(currentSubscription);
        billingLog.setSubscriptionStart(LocalDateTime.now());
        billingLog.setOrganizationId(organization);


        organization.setSubscription(currentSubscription);
        organizationRepository.save(organization);
        subscriptionRepository.save(currentSubscription);
        billingLogRepository.save(billingLog);
    }
}
