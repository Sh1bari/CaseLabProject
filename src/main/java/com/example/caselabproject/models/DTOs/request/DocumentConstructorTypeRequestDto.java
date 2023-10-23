package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DocumentConstructorTypeRequestDto {

    @NotBlank
    private String name;

    public DocumentConstructorType mapToEntity() {
        return DocumentConstructorType.builder()
                .name(this.name)
                .build();
    }
}
