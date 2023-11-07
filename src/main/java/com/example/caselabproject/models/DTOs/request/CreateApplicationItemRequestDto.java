package com.example.caselabproject.models.DTOs.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Description:
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationItemRequestDto {
    @Min(value = 1L, message = "User id cant be less than 1.")
    private Long toUserId;

    @NotNull(message = "Department id cant be null.")
    private Long toDepartmentId;

}
