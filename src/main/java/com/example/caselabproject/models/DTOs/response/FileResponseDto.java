package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {

    private Long id;

    private String name;

    private Double size;

    private String path;

    private String type;

    public static FileResponseDto mapFromEntity(File file) {
        return FileResponseDto.builder()
                .id(file.getId())
                .name(file.getName())
                .size(file.getSize())
                .path(file.getPath())
                .type(file.getType())
                .build();
    }
}
