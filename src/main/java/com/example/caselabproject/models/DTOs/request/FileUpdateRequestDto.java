package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.File;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileUpdateRequestDto {

    @NotBlank
    private String name;

    public File mapToEntity() {
        return File.builder()
                .name(this.name)
                .build();
    }
}
