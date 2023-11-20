package com.example.caselabproject.models.DTOs.request.role;

import com.example.caselabproject.models.entities.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Builder
@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    @NotBlank
    private String name;

    public static RoleDto mapFromEntity(Role role) {
        return RoleDto.builder()
                .name(role.getName())
                .build();
    }
}
