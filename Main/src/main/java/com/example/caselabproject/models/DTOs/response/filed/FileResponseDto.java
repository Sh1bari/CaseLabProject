package com.example.caselabproject.models.DTOs.response.filed;

import com.example.caselabproject.models.entities.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {

    private Long id;

    private String name;

    private String type;

    private Long size;

    private String path;

    public static FileResponseDto mapFromEntity(File file) {
        return FileResponseDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .size(file.getSize())
                .path(file.getPath())
                .build();
    }
}
