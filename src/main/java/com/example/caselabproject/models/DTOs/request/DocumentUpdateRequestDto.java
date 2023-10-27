package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.Document;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class DocumentUpdateRequestDto {

    @NotBlank
    private String name;

    private LocalDateTime updateDate;

    public Document mapToEntity() {
        return Document.builder()
                .name(this.name)
                .updateDate(LocalDateTime.now())
                .build();
    }
}
