package com.example.caselabproject.models.DTOs.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordRequest {
    @NotBlank(message = "Password cant be blank")
    private String password;
}
