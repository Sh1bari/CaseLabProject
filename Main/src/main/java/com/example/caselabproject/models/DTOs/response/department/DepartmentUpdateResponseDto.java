package com.example.caselabproject.models.DTOs.response.department;

import com.example.caselabproject.models.entities.Department;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentUpdateResponseDto {
    private Long id;
    private String name;
    private String serialKey;

    public static DepartmentUpdateResponseDto mapFromEntity(Department department) {
        return DepartmentUpdateResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .serialKey(department.getSerialKey())
                .build();
    }
}
