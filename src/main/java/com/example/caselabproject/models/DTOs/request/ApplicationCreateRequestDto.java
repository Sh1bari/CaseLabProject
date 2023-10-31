package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.Document;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationCreateRequestDto {
    @Future(message = "Deadline cant be less than now")
    private LocalDateTime deadlineDate;

    public Application mapToEntity() {
        return Application.builder()
                .creationDate(LocalDateTime.now())
                .deadlineDate(deadlineDate)
                .build();

    }
}
