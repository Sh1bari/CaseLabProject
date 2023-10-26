package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentCreateResponseDto {

    private Long id;
    private String name;
    private LocalDateTime creationDate;

    public static DocumentCreateResponseDto mapFromEntity(Document document){
        return DocumentCreateResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .creationDate(document.getCreationDate())
                .build();
    }
}
