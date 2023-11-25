package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.services.DepartmentService;
import com.example.caselabproject.services.UserService;
import com.example.caselabproject.validation.annotations.CheckOrganization;
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
    @CheckOrganization(serviceClass = UserService.class)
    @Min(value = 1L, message = "User id cant be less than 1.")
    private Long toUserId;

    @CheckOrganization(serviceClass = DepartmentService.class)
    @NotNull(message = "Department id cant be null.")
    @Min(value = 1L, message = "Department id cant be less than 1.")
    private Long toDepartmentId;

}
