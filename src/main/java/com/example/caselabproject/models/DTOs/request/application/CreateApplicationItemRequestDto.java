package com.example.caselabproject.models.DTOs.request.application;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationItemRequestDto {

    @NotNull(message = "Department id cant be null.")
    private Long toDepartmentId;

}
