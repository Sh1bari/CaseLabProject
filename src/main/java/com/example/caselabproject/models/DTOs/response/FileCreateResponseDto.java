package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileCreateResponseDto {

    private Long id;

    private String name;

    private Double size;

    private String path;

    private String type;

    public static FileCreateResponseDto mapFromEntity(File file) {
        return FileCreateResponseDto.builder()
                .id(file.getId())
                .name(file.getName())
                .size(file.getSize())
                .path(file.getPath())
                .type(file.getType())
                .build();
    }
}
