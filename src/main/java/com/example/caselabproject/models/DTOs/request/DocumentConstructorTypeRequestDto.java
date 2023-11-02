package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
public class DocumentConstructorTypeRequestDto {
    @NotBlank(message = "name must not be null and must contain at least one non-whitespace character.")
    private String name;

    @NotEmpty(message = "prefix must not be null or empty.")
    private String prefix;

    @NotNull(message = "fields must not be null.")
    @UniqueElements(message = "fields must contain only unique elements.")
    private List<@Valid FieldRequestDto> fields;

    public DocumentConstructorType mapToEntity() {
        return DocumentConstructorType.builder()
                .name(this.name)
                .fields(this.fields.stream().map(FieldRequestDto::mapToEntity).toList())
                .prefix(this.prefix)
                .build();
    }
}
