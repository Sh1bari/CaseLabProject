package com.example.caselabproject.models.DTOs.request.application;

import com.example.caselabproject.models.entities.Application;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDeleteRequestDto {
    @Min(value = 1, message = "id can not be less 1")
    private Long id;

    public Application mapToEntity() {
        return Application.builder()
                .id(id)
                .build();
    }
}
