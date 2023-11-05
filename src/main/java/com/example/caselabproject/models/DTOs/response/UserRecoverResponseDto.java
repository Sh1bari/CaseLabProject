package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
public class UserRecoverResponseDto {

    private Long id;
    private String position;
    private String username;
    private String email;
    private Department department;
    private RecordState recordState;
    private List<RoleDto> roles;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;

    public static UserRecoverResponseDto mapFromEntity(User user) {
        return UserRecoverResponseDto.builder()
                .id(user.getId())
                .position(user.getPosition())
                .username(user.getUsername())
                .recordState(RecordState.ACTIVE)
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
