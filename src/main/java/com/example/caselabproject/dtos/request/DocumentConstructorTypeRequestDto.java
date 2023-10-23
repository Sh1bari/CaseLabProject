package com.example.caselabproject.dtos.request;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DocumentConstructorTypeRequestDto {

    @NotBlank
    private String name;

    public DocumentConstructorType mapToEntity() {
        DocumentConstructorType type = new DocumentConstructorType();
        type.setName(this.name);
        return type;
    }
}
