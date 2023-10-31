package com.example.caselabproject.models.DTOs;

import com.example.caselabproject.models.entities.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {

    private Long id;

    public static FileDto mapFromEntity(File file) {
        return FileDto.builder()
                .id(file.getId())
                .build();
    }
}
