package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.multitenancy.annotations.CheckOrganization;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import jakarta.validation.constraints.Min;
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

    @CheckOrganization(repositoryClass = DocumentConstructorTypeRepository.class)
    @Min(value = 1L, message = "Constructor type id must be >= 1")
    private Long constructorTypeId;

    public Document mapToEntity() {
        return Document.builder()
                .name(name)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
