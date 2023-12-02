package com.example.caselabproject.models.DTOs.request.department;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DepartmentChildDto {

    @Min(value = 1, message = "Id can not be less 1")
    private Long id;
}
