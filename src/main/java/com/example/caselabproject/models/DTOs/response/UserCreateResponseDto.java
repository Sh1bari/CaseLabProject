package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.User;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponseDto {

    private Long id;
    private String position;
    private String username;
    private String email;
    private List<RoleDto> roles;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;

    public static UserCreateResponseDto mapFromEntity(User user) {
        return UserCreateResponseDto.builder()
                .id(user.getId())
                .position(user.getPosition())
                .username(user.getUsername())
                .email(user.getAuthUserInfo().getEmail())
                .roles(user.getRoles().stream().map(RoleDto::mapFromEntity).toList())
                .firstName(user.getPersonalUserInfo().getFirstName())
                .lastName(user.getPersonalUserInfo().getLastName())
                .patronymic(user.getPersonalUserInfo().getPatronymic())
                .birthDate(user.getPersonalUserInfo().getBirthDate())
                .build();
    }
}
