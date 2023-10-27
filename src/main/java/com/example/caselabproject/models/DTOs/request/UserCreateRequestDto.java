package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserCreateRequestDto {

    private String position;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private String email;

    @NotBlank
    private List<RoleDto> roles;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String patronymic;

    public User mapToEntity() {

        return User.builder()

                .position(position)
                .username(username)
                .recordState(RecordState.ACTIVE)
                .
                .build();
    }
}
