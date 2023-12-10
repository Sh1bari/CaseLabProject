package com.example.caselabproject.models.DTOs.response.department;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentGetAllResponseDto {
    private Long id;
    private String name;
    private String serialKey;
    private Integer amountOfEmployee;

    public static DepartmentGetAllResponseDto mapFromEntity(Department department) {
        return DepartmentGetAllResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .serialKey(department.getSerialKey())
                .amountOfEmployee((int) department.getUsers()
                        .stream()
                        .filter(user -> user.getRecordState() == RecordState.ACTIVE)
                        .count())
                .build();
    }
}
