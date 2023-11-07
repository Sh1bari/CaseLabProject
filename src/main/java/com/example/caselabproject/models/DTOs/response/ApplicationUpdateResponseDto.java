package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationUpdateResponseDto {
    private Long id;
    private String name;
    private LocalDateTime deadline;

    public static ApplicationUpdateResponseDto mapFromEntity(Application application) {
        return ApplicationUpdateResponseDto.builder()
                .id(application.getId())
                .name(application.getName())
                .deadline(application.getDeadlineDate())
                .build();
    }
}
