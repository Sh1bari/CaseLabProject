package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import lombok.*;

import java.util.List;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentConstructorTypeRecoverResponseDto {
    private Long id;
    private String name;
    private String prefix;
    private List<FieldResponseDto> fields;
    private RecordState state;

    public static DocumentConstructorTypeRecoverResponseDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeRecoverResponseDto.builder()
                .id(type.getId())
                .name(type.getName())
                .prefix(type.getPrefix())
                .fields(type.getFields().stream().map(FieldResponseDto::mapFromEntity).toList())
                .state(type.getRecordState())
                .build();
    }
}
