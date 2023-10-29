package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Application;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDeleteRequestDto {
    private Long id;

    public Application mapToEntity(){
        Application application = new Application();
        application.setId(id);
        return application;
    }
}
