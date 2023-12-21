package com.example.caselabproject.models.DTOs.request.user;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.AuthUserInfo;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Data
@Validated
public class UserCreateRequestDto {

    private String position;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password must not be blank")
    private String password;
    @Email(message = "Email is not valid")
    private String email;
    @NotEmpty(message = "Roles must not be empty")
    private List<@Valid RoleDto> roles;
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    @NotBlank(message = "Last name must not be blank")
    private String lastName;
    private Boolean isDirector;

    private String patronymic;
    @Past(message = "Date of birth can't be more than current date")
    private LocalDate birthDate;

    public User mapToEntity() {
        User user = User.builder()
                .position(position)
                .username(username)
                .recordState(RecordState.ACTIVE)
                .authUserInfo(AuthUserInfo.builder()
                        .email(email)
                        .build())
                .isDirector(false)
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
