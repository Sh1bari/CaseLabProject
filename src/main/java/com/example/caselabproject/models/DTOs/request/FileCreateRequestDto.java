package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.File;
import lombok.Data;

@Data
public class FileCreateRequestDto {

    private String name;

    private Double size;

    private String path;

    private String type;



    public File mapToEntity() {
        return File.builder()
                .name(this.name)
                .size(this.size)
                .path(this.path)
                .type(this.type)
                .build();
    }
}
