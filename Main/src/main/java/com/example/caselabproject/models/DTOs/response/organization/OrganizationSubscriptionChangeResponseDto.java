package com.example.caselabproject.models.DTOs.response.organization;

import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationSubscriptionChangeResponseDto {

    private Long id;
    private String orgName;
    private String subscriptionName;

    public static OrganizationSubscriptionChangeResponseDto mapFromEntity(Organization organization) {
        return OrganizationSubscriptionChangeResponseDto.builder()
                .id(organization.getId())
                .orgName(organization.getName())
                .subscriptionName(organization.getSubscription().getSubscriptionName())
                .build();
    }

}
