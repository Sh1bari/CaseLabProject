package com.example.caselabproject.models.DTOs.request.field;

import com.example.caselabproject.models.entities.Field;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
