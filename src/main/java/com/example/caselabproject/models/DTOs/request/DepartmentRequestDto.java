package com.example.caselabproject.models.DTOs.request;
import com.example.caselabproject.models.entities.Department;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class DepartmentRequestDto {
    @NotBlank
    private String name;

    public Department mapToEntity() {
        return Department.builder()
                .name(this.name)
                .build();
    }
}
