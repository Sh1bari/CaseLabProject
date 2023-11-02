package com.example.caselabproject.models.DTOs.request;

import com.example.caselabproject.models.entities.File;
import lombok.Data;

@Data
public class FileCreateRequestDto {

    //@NotBlank(message = "File name can not be blank")
    private String name;

    //@NotBlank
    private String type;

    //@NotBlank
    private byte[] bytes;

    //@NotBlank
    private Long size;

    //@NotBlank
    private String path;

    public File mapToEntity() {
        return File.builder()
                .name(this.name)
                .type(this.type)
                .bytes(this.bytes)
                .size(this.size)
                .path(this.path)
                .build();
    }
}
