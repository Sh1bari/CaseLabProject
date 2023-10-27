package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class DocumentCreateRequestDto {

    @NotBlank
    private String name;

    public Document mapToEntity() {
        return Document.builder()
                .name(this.name)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
