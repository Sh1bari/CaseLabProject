package com.example.caselabproject.models.DTOs.response.department;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DepartmentGetByIdResponseDto {
    private Long id;
    private String name;
    private String serialKey;
    private Integer amountOfEmployee;
    private Long parentId;
    List<Long> childDepartmentsId;


    public static DepartmentGetByIdResponseDto mapFromEntity(Department department) {
        return DepartmentGetByIdResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .serialKey(department.getSerialKey())
                .amountOfEmployee((int) department.getUsers()
                        .stream()
                        .filter(user -> user.getRecordState() == RecordState.ACTIVE)
                        .count())
                .parentId(checkParentId(department))
                .childDepartmentsId(department.getChildDepartments().stream().map(Department::getId).toList())
                .build();
    }

    /**
     * Внутренний метод, проверяющий, есть ли родитель у данного департамента.
     * Возвращает либо id родителя, либо null.
     */
    private static Long checkParentId(Department department) {

        if (department.getParentDepartment() != null)
            return department.getParentDepartment().getId();

        return null;
    }
}
