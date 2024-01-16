package com.example.caselabproject.models.DTOs.response.user;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatarResponseDto {

    private Long id;
    private String username;
    private String avatarPath;

    public static UserAvatarResponseDto mapFromEntity(User user) {
        return UserAvatarResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarPath(user.getAvatarPath().getPath())
                .build();
    }

}
