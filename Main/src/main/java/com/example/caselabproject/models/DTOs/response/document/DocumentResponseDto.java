package com.example.caselabproject.models.DTOs.response.document;

import com.example.caselabproject.models.DTOs.FileDto;
import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Slf4j
public class DocumentResponseDto {

    private Long id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private Long creatorId;

    private List<FileDto> files;

    private Long documentConstructorTypeId;

    private Map<String, String> fieldsValues;

    public static DocumentResponseDto mapFromEntity(Document document) {
        Map<String, String> stringFieldsValues = new HashMap<>();
        document.getFieldsValues().forEach(
                (field, value) -> stringFieldsValues.put(field.getName(), value));

        return DocumentResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .documentConstructorTypeId(document.getDocumentConstructorType().getId())
                .fieldsValues(stringFieldsValues)
                .creationDate(document.getCreationDate())
                .updateDate(document.getUpdateDate())
                .creatorId(document.getCreator().getId())
                .files(document.getFiles().stream().map(FileDto::mapFromEntity).toList())
                .build();
    }
}
