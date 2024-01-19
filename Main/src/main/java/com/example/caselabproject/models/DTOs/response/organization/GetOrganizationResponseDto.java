package com.example.caselabproject.models.DTOs.response.organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOrganizationResponseDto {
    private String name;
}
