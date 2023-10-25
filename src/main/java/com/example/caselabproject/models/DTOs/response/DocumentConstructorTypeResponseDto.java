package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentConstructorTypeResponseDto {

    private Long id;
    private String name;
    private List<FieldResponseDto> fields;

    public static DocumentConstructorTypeResponseDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeResponseDto.builder()
                .id(type.getId())
                .name(type.getName())
                .fields(type.getFields().stream().map(FieldResponseDto::mapFromEntity).toList())
                .build();
    }
}
