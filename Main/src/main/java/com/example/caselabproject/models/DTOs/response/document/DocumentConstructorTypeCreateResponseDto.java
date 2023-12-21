package com.example.caselabproject.models.DTOs.response.document;

import com.example.caselabproject.models.DTOs.response.filed.FieldResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class DocumentConstructorTypeCreateResponseDto {
    private Long id;
    private String name;
    private String prefix;
    private List<FieldResponseDto> fields;
    private RecordState state;

    public static DocumentConstructorTypeCreateResponseDto mapFromEntity(DocumentConstructorType type) {
        return DocumentConstructorTypeCreateResponseDto.builder()
                .id(type.getId())
                .name(type.getName())
                .prefix(type.getPrefix())
                .fields(type.getFields().stream().map(FieldResponseDto::mapFromEntity).toList())
                .state(type.getRecordState())
                .build();
    }
}
