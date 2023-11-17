package com.example.caselabproject.models.DTOs.request.department;

import com.example.caselabproject.models.entities.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequestDto {
    @NotBlank(message = "Department name can't be blank.")
    private String name;

    public Department mapToEntity() {
        return Department.builder()
                .name(this.name)
                .build();
    }
}
