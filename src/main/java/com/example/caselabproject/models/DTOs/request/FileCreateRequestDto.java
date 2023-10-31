package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.File;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileCreateRequestDto {

    @NotBlank(message = "File name can not be blank")
    private String name;

    @NotBlank
    private Double size;

    @NotBlank
    private String path;

    @NotBlank
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
