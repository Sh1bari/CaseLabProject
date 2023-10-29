package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationCreateResponseDto {
    private Long id;
    private LocalDateTime creationDate;

    public static ApplicationCreateResponseDto mapFromEntity(Application application){
        return ApplicationCreateResponseDto.builder()
                .id(application.getId())
                .creationDate(application.getCreationDate())
                .build();
    }
}
