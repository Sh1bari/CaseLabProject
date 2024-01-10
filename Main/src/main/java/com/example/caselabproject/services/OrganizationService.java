package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.organization.OrganizationChangeNameRequestDto;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationChangeNameResponseDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.security.Principal;

public interface OrganizationService {

//    OrganizationSubscriptionChangeResponseDto raiseSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto);
//
//    OrganizationSubscriptionChangeResponseDto lowerSubscription(OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto);

    OrganizationSubscriptionChangeResponseDto changeSubscription(@Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto, @NotBlank(message = "Organization user username can't be blank.") String username);

    OrganizationChangeNameResponseDto changeOrganizationName(@Valid OrganizationChangeNameRequestDto organizationChangeNameRequestDto, @NotBlank(message = "Organization user username can't be blank.") String username);

}
