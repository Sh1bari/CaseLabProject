package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentConstructorTypeResponseDto {

    private Long id;
    private String name;

    public static DocumentConstructorTypeResponseDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeResponseDto.builder()
                .id(type.getId())
                .name(type.getName())
                .build();
    }
}
