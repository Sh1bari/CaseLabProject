package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequestDto {

    private String position;
    private RecordState recordState;
    @NotBlank
    private String username;


    public User mapToEntity() {
        return User.builder()
                .position(this.position)
                .recordState(this.recordState)
                .username(this.username)
                .build();
    }
}
