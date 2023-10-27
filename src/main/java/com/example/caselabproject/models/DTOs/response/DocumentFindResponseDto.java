package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentFindResponseDto {

    private Long id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public static DocumentFindResponseDto mapFromEntity(Document document) {
        return DocumentFindResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .creationDate(document.getCreationDate())
                .updateDate(document.getUpdateDate())
                .build();
    }
}
