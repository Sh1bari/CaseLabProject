package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequestDto {

    @NotBlank(message = "Document name can not be blank")
    private String name;

    @NotBlank(message = "Constructor type can not be blank")
    private String constructorTypeName;

    public Document mapToEntity() {
        return Document.builder()
                .name(name)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
