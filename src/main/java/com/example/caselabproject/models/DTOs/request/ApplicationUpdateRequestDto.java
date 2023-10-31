package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationUpdateRequestDto {
    @Future(message = "Deadline cant be less than now")
    private LocalDateTime deadline;
    public Application mapToEntity() {
        return Application.builder()
                .deadlineDate(deadline)
                .build();
    }
}
