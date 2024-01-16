package com.example.caselabproject.models.DTOs.request.organization;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizationChangeNameRequestDto {
    @NotBlank(message = "Organization name must not be blank")
    private String name;
}
