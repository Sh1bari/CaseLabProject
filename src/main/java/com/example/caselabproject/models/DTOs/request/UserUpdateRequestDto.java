package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.AuthUserInfo;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.User;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@Validated
public class UserUpdateRequestDto {
    private String position;
    private String username;

    private Long departmentId;

    private String password;
    @Email(message = "Email is not valid")
    private String email;
    private List<@Valid RoleDto> roles;
    private String firstName;

    private String lastName;

    private String patronymic;
    @Past(message = "Date of birth cant be more than current date")
    private LocalDate birthDate;
  
    public User mapToEntity() {
        User user = User.builder()
                .position(position)
                .username(username)
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
