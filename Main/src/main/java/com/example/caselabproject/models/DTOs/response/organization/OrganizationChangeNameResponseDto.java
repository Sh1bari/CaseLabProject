package com.example.caselabproject.models.DTOs.response.organization;

import com.example.caselabproject.models.entities.Organization;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationChangeNameResponseDto {
    private Long id;
    private String name;


    public static OrganizationChangeNameResponseDto mapFromEntity(Organization organization) {
        return OrganizationChangeNameResponseDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }
}
