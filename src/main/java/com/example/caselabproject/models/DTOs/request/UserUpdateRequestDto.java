package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.DTOs.RoleDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateRequestDto {
    private String position;

    private String username;
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
}
