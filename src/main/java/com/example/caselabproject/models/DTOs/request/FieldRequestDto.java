package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Field;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FieldRequestDto {

    @NotBlank(message = "name must not be null and must contain at least one non-whitespace character.")
    private String name;

    public Field mapToEntity() {
        return Field.builder()
                .name(this.name)
                .build();
    }
}
