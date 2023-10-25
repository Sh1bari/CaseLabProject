package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Field;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FieldRequestDto {

    @NotBlank
    private String name;

    public Field mapToEntity() {
        return Field.builder()
                .name(this.name)
                .build();
    }
}
