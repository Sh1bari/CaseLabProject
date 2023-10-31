package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponseDto {

    private Long id;
    private String name;
    private Integer amountOfEmployee;

    public static DepartmentResponseDto mapFromEntity(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .amountOfEmployee((int) department.getUsers()
                        .stream()
                        .filter(user -> user.getRecordState() == RecordState.ACTIVE)
                        .count())
                .build();
    }
}
