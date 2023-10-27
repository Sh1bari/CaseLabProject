package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentUpdateResponseDto {


    private Long id;

    private String name;

    private LocalDateTime updateDate;

    public static DocumentUpdateResponseDto mapFromEntity(Document document) {
        return DocumentUpdateResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .updateDate(document.getUpdateDate())
                .build();
    }
}
