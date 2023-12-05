package com.example.caselabproject.models.DTOs.request.document;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DocumentConstructorTypePatchRequestDto {
    @NotBlank(message = "name must not be null and must contain at least one non-whitespace character.")
    private String name;

    @NotEmpty(message = "prefix must not be null or empty.")
    private String prefix;

    public DocumentConstructorType mapToEntity() {
        return DocumentConstructorType.builder()
                .name(this.name)
                .prefix(this.prefix)
                .build();
    }
}
