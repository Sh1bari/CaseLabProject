package com.example.caselabproject.services.implementations;


import com.example.caselabproject.exceptions.organization.OrganizationNotFoundException;
import com.example.caselabproject.exceptions.subscription.SubscriptionNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationChangeNameRequestDto;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationChangeNameResponseDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;
import com.example.caselabproject.models.entities.BillingLog;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.SubscriptionName;
import com.example.caselabproject.repositories.BillingLogRepository;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.SubscriptionRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@Validated
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final SubscriptionRepository subscriptionRepository;
    private final BillingLogRepository billingLogRepository;
    private final UserRepository userRepo;

    @Override
    public OrganizationSubscriptionChangeResponseDto changeSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Long organizationId = user.getOrganization().getId();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        Subscription currentSubscription = organization.getSubscription();


        Subscription neededSubscription = subscriptionRepository.findById(subscriptionChangeRequestDto.getId())
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionChangeRequestDto.getId()));

        setSubscription(currentSubscription, organization, neededSubscription);

        return OrganizationSubscriptionChangeResponseDto.mapFromEntity(organization);
    }

    @Override
    public OrganizationChangeNameResponseDto changeOrganizationName(OrganizationChangeNameRequestDto organizationChangeNameRequestDto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Long organizationId = user.getOrganization().getId();

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        organization.setName(organizationChangeNameRequestDto.getName());

        organizationRepository.save(organization);

        return OrganizationChangeNameResponseDto.mapFromEntity(organization);
    }


    private void setSubscription(Subscription currentSubscription, Organization organization, Subscription neededSubscription) {

        BillingLog billingLog = BillingLog.builder()
                .lastSubscription(currentSubscription)
                .currentSubscription(neededSubscription)
                .subscriptionStart(LocalDateTime.now())
                .organizationId(organization)
                .build();

        organization.setSubscription(currentSubscription);
        organizationRepository.save(organization);
        billingLogRepository.save(billingLog);
    }
}

//    @Override
//    // прередавать id подписки
//    // id орг через принципал
//    // проверка количество людей из организации и доступ к более низкому тарифу
//    public OrganizationSubscriptionChangeResponseDto raiseSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto) {
//        Organization organization = organizationRepository.getReferenceById(subscriptionChangeRequestDto.getId());
//        Subscription currentSubscription = organization.getSubscription();
//        BillingLog billingLog = new BillingLog();
//
//        switch (currentSubscription.getSubscriptionName()) {
//            case DEFAULT -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.BASIC);
//            case BASIC -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.STANDARD);
//            case STANDARD ->
//                    setSubscription(billingLog, currentSubscription, organization, SubscriptionName.ENTERPRISE);
//        }
//
//        return OrganizationSubscriptionChangeResponseDto.mapFromEntity(organization);
//    }

//    @Override
//    public OrganizationSubscriptionChangeResponseDto lowerSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto) {
//        Organization organization = organizationRepository.getReferenceById(subscriptionChangeRequestDto.getId());
//        Subscription currentSubscription = organization.getSubscription();
//        BillingLog billingLog = new BillingLog();
//
//        switch (currentSubscription.getSubscriptionName()) {
//            case ENTERPRISE ->
//                    setSubscription(billingLog, currentSubscription, organization, SubscriptionName.STANDARD);
//            case STANDARD -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.BASIC);
//            case BASIC -> setSubscription(billingLog, currentSubscription, organization, SubscriptionName.DEFAULT);
//        }
//
//        return OrganizationSubscriptionChangeResponseDto.mapFromEntity(organization);
//    }