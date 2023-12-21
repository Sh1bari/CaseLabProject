package com.example.caselabproject.models.DTOs.request.document;

import com.example.caselabproject.models.DTOs.request.field.FieldRequestDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DocumentConstructorTypeRequestDto {
    @NotBlank(message = "name must not be null and must contain at least one non-whitespace character.")
    private String name;

    @NotEmpty(message = "prefix must not be null or empty.")
    private String prefix;

    @NotNull(message = "fields must not be null.")
    @UniqueElements(message = "fields must contain only unique elements.")
    private List<@Valid FieldRequestDto> fields = new ArrayList<>();

    public DocumentConstructorType mapToEntity() {
        return DocumentConstructorType.builder()
                .name(this.name)
                .fields(this.fields.stream().map(FieldRequestDto::mapToEntity).collect(Collectors.toList()))
                .prefix(this.prefix)
                .build();
    }
}
