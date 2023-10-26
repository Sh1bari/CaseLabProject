package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentConstructorTypeResponseDto {

    private Long id;
    private String name;
    private String prefix;
    private List<FieldResponseDto> fields;
    private RecordState state;

    public static DocumentConstructorTypeResponseDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeResponseDto.builder()
                .id(type.getId())
                .name(type.getName())
                .prefix(type.getPrefix())
                .fields(type.getFields().stream().map(FieldResponseDto::mapFromEntity).toList())
                .state(type.getRecordState())
                .build();
    }
}
