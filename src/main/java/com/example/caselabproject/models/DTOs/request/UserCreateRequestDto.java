package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.AuthUserInfo;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.implementations.RoleServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@Validated
public class UserCreateRequestDto {

    private String position;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty
    private List<@Valid RoleDto> roles;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String patronymic;
    @Past(message = "Date of birth cant be more than current date")
    private LocalDate birthDate;

    public User mapToEntity() {
        User user = User.builder()
                .position(position)
                .username(username)
                .recordState(RecordState.ACTIVE)
                .authUserInfo(AuthUserInfo.builder()
                        .email(email)
                        .build())
                .personalUserInfo(PersonalUserInfo.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .patronymic(patronymic)
                        .birthDate(birthDate)
                        .build())
                .build();
        user.getPersonalUserInfo().setUser(user);
        user.getAuthUserInfo().setUser(user);
        return user;
    }
}
