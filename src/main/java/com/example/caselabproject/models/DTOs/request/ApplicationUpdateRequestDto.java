package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationUpdateRequestDto {
    private LocalDateTime deadline;
    public Application mapToEntity(){
        Application application = new Application();
        application.setDeadlineDate(deadline);
        return application;
    }
}
