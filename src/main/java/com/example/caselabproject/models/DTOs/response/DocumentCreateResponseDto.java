package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DocumentCreateResponseDto {

    private Long id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private Long creatorId;

    private Map<String, String> fieldsValues;

    public static DocumentCreateResponseDto mapFromEntity(Document document) {
        Map<String, String> stringFieldsValues = new HashMap<>();
        document.getFieldsValues().forEach(
                (field, value) -> stringFieldsValues.put(field.getName(), value));

        return DocumentCreateResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .creatorId(document.getCreator().getId())
                .creationDate(document.getCreationDate())
                .updateDate(document.getUpdateDate())
                .creatorId(document.getCreator().getId())
                .fieldsValues(stringFieldsValues)
                .build();
    }

    public static List<DocumentCreateResponseDto> mapFromListOfEntities(List<Document> document) {
        List<DocumentCreateResponseDto> res = new ArrayList<>();
        document.forEach(o -> {
            res.add(DocumentCreateResponseDto.builder()
                    .id(o.getId())
                    .name(o.getName())
                    .creationDate(o.getCreationDate())
                    .build());
        });
        return res;
    }
}
