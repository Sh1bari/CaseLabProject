package com.example.caselabproject.models.DTOs.response.user;

import com.example.caselabproject.models.DTOs.request.role.RoleDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDeleteResponseDto {

    private Long id;
    private String position;
    private String username;
    private String email;
    private Department department;
    private List<RoleDto> roles;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;

    public static UserDeleteResponseDto mapFromEntity(User user) {
        return UserDeleteResponseDto.builder()
                .id(user.getId())
                .position(user.getPosition())
                .username(user.getUsername())
                .email(user.getAuthUserInfo().getEmail())
                .department(user.getDepartment())
                .roles(user.getRoles().stream().map(RoleDto::mapFromEntity).toList())
                .firstName(user.getPersonalUserInfo().getFirstName())
                .lastName(user.getPersonalUserInfo().getLastName())
                .patronymic(user.getPersonalUserInfo().getPatronymic())
                .birthDate(user.getPersonalUserInfo().getBirthDate())
                .build();
    }
}