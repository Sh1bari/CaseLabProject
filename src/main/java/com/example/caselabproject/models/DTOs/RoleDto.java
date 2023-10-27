package com.example.caselabproject.models.DTOs;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class RoleDto {
    @NotBlank
    private String name;
}
