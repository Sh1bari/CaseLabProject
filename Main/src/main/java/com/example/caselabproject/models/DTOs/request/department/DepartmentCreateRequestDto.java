package com.example.caselabproject.models.DTOs.request.department;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DepartmentCreateRequestDto {

    @NotBlank(message = "Department name can't be blank.")
    private String name;

    public Department mapToEntity() {
        return Department.builder()
                .name(this.name)
                .recordState(RecordState.ACTIVE)
                .users(new ArrayList<>())
                .childDepartments(new ArrayList<>())
                .build();
    }
}
