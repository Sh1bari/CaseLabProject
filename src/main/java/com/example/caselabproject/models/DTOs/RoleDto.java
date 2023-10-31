package com.example.caselabproject.models.DTOs;

import com.example.caselabproject.models.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

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
