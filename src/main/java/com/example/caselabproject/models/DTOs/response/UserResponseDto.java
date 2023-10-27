package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String position;
    private String username;
    private String department;

    public static UserResponseDto mapFromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .position(user.getPosition())
                .username(user.getUsername())
                .department(user.getDepartment().getName())
                .build();
    }
}
