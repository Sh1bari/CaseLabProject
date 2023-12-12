package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;

public interface OrganizationService {

    public OrganizationSubscriptionChangeResponseDto raiseSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto);

    public OrganizationSubscriptionChangeResponseDto lowerSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto);

}
