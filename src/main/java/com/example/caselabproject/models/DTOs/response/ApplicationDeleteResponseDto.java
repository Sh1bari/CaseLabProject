package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDeleteResponseDto {
    private Long id;
    public static ApplicationDeleteResponseDto mapFromEntity(Application application){
        return ApplicationDeleteResponseDto.builder()
                .id(application.getId())
                .build();
    }
}
