package com.example.caselabproject.dtos.request;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DocumentConstructorTypeRequestDto {

    @NotEmpty
    private String name;

    public DocumentConstructorType mapToEntity() {
        DocumentConstructorType type = new DocumentConstructorType();
        type.setName(this.name);
        return type;
    }
}
