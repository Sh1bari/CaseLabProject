package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.Document;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationCreateRequestDto {
    private LocalDateTime deadlineDate;
    private Document document;

    public Application mapToEntity(){
        Application application = new Application();
        application.setCreationDate(LocalDateTime.now());
        application.setDeadlineDate(this.deadlineDate);
        application.setDocument(document);
        return application;
    }
}
