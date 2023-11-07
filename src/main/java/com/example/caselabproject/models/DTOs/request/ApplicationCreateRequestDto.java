package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.Document;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationCreateRequestDto {
    @NotBlank(message = "Application name can't be blank.")
    private String name;
    @Future(message = "Deadline cant be less than now")
    private LocalDateTime deadlineDate;

    public Application mapToEntity() {
        return Application.builder()
                .name(name)
                .creationDate(LocalDateTime.now())
                .deadlineDate(deadlineDate)
                .build();

    }
}
