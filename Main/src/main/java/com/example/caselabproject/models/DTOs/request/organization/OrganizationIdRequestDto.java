package com.example.caselabproject.models.DTOs.request.organization;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OrganizationIdRequestDto {
    @Min(value = 1, message = "Id can not be less than 1")
    private Long id;
}
