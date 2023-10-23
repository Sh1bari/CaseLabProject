package com.example.caselabproject.dtos;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentConstructorTypeDto {
    private String documentType;

    public static DocumentConstructorTypeDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeDto.builder()
                .documentType(type.getName())
                .build();
    }
}
