package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationFindResponseDto {
    private Long id;
    private Document document;
    private LocalDateTime creationDate;
    private LocalDateTime deadlineDate;

    public static ApplicationFindResponseDto mapFromEntity(Application application){
        return ApplicationFindResponseDto.builder()
                .id(application.getId())
                .document(application.getDocument())
                .creationDate(application.getCreationDate())
                .deadlineDate(application.getDeadlineDate())
                .build();
    }
}
