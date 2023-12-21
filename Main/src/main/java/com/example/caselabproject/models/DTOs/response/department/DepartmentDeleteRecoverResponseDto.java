package com.example.caselabproject.models.DTOs.response.department;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDeleteRecoverResponseDto {

    private Long id;
    private String name;
    private String serialKey;
    private RecordState recordState;

    public static DepartmentDeleteRecoverResponseDto mapFromEntity(Department department) {
        return DepartmentDeleteRecoverResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .serialKey(department.getSerialKey())
                .recordState(department.getRecordState())
                .build();
    }
}
