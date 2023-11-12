package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.DTOs.FileDto;
import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

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

    public static DocumentResponseDto mapFromEntity(Document document) {

        Long documentConstructorTypeId = null;

        try {
            documentConstructorTypeId = document.getDocumentConstructorType().getId();
        } catch (Exception e) {
            log.warn("DocumentConstructorType name is empty");
        }

        return DocumentResponseDto.builder()
                .id(document.getId())
                .name(document.getName())
                .documentConstructorTypeId(documentConstructorTypeId)
                .creationDate(document.getCreationDate())
                .updateDate(document.getUpdateDate())
                .creatorId(document.getCreator().getId())
                .files(document.getFiles().stream().map(FileDto::mapFromEntity).toList())
                .build();
    }
}
