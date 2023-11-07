package com.example.caselabproject.models.DTOs;


import com.example.caselabproject.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;

    public static UserDto mapFromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getAuthUserInfo().getEmail())
                .build();
    }
}
