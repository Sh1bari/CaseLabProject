package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserUpdateResponseDto {

    private Long id;
    private String position;
    private String username;
    private String email;
    private Long departmentId;
    private List<RoleDto> roles;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;

    public static UserUpdateResponseDto mapFromEntity(User user) {
        Long departmentId = null;
        try{
            departmentId = user.getDepartment().getId();
        }catch (Exception e){}
        UserUpdateResponseDto builder = UserUpdateResponseDto.builder()
                .id(user.getId())
                .position(user.getPosition())
                .username(user.getUsername())
                .email(user.getAuthUserInfo().getEmail())
                .departmentId(departmentId)
                .roles(user.getRoles().stream().map(RoleDto::mapFromEntity).toList())
                .firstName(user.getPersonalUserInfo().getFirstName())
                .lastName(user.getPersonalUserInfo().getLastName())
                .patronymic(user.getPersonalUserInfo().getPatronymic())
                .birthDate(user.getPersonalUserInfo().getBirthDate())
                .build();
        return builder;
    }
}