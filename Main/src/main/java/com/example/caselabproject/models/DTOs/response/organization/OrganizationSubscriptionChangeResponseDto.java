package com.example.caselabproject.models.DTOs.response.organization;

import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationSubscriptionChangeResponseDto {

    private Long id;
    private String name;
    private Subscription subscription;

    public static OrganizationSubscriptionChangeResponseDto mapFromEntity(Organization organization) {
        return OrganizationSubscriptionChangeResponseDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .subscription(organization.getSubscription())
                .build();
    }

}