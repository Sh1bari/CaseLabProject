package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DocumentCreateResponseDto {

    private Long id;
    private String name;
    private LocalDateTime creationDate;

    public static DocumentCreateResponseDto mapFromEntity(Document document) {
        return DocumentCreateResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .creationDate(document.getCreationDate())
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
