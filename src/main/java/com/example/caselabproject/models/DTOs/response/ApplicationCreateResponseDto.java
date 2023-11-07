package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationCreateResponseDto {
    private Long id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime deadlineDate;
    private Long creatorId;

    public static ApplicationCreateResponseDto mapFromEntity(Application application){
        return ApplicationCreateResponseDto.builder()
                .id(application.getId())
                .name(application.getName())
                .deadlineDate(application.getDeadlineDate())
                .creationDate(application.getCreationDate())
                .creatorId(application.getCreatorId().getId())
                .build();
    }
}
