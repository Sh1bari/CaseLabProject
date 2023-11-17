package com.example.caselabproject.models.DTOs.response.field;

import com.example.caselabproject.models.entities.Field;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldResponseDto {

    private Long id;
    private String name;

    public static FieldResponseDto mapFromEntity(Field field) {
        return FieldResponseDto.builder()
                .id(field.getId())
                .name(field.getName())
                .build();
    }
}
