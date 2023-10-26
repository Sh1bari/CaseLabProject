package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Department;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponseDto {

    private Long id;
    private String name;

    public static DepartmentResponseDto mapFromEntity(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
