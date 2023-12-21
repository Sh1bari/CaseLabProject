package com.example.caselabproject.models.DTOs.request.application;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectApplicationItemRequestDto {
    @Min(value = 1L, message = "User id cant be less than 1.")
    private Long toUserId;

    @NotNull(message = "Department id cant be null.")
    private Long toDepartmentId;
}
