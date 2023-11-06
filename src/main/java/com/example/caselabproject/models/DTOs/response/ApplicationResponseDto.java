package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationResponseDto {
    private Long id;
    public static ApplicationResponseDto mapFromEntity(Application application){
        return ApplicationResponseDto.builder()
                .id(application.getId())
                .build();
    }
}
