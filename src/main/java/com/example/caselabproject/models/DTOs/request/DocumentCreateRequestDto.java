package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class DocumentCreateRequestDto {

    public DocumentCreateRequestDto(String name){
        this.name = name;
        this.creationDate = LocalDateTime.now();
    }

    private String name;
    private LocalDateTime creationDate;

    public Document mapToEntity(){
        return Document.builder()
                .name(this.name)
                .creationDate(this.creationDate)
                .build();
    }

}
