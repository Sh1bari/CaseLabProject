package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationUpdateStatusAndCommentResponseDto {
    private Long id;
    private ApplicationResponseDto application;
    private ApplicationItemStatus status;
    private String comment;

    public static ApplicationUpdateStatusAndCommentResponseDto mapFromEntity(ApplicationItem applicationItem){
        return ApplicationUpdateStatusAndCommentResponseDto.builder()
                .application(ApplicationResponseDto.mapFromEntity(applicationItem.getApplication()))
                .id(applicationItem.getId())
                .status(applicationItem.getStatus())
                .comment(applicationItem.getComment())
                .build();
    }
}
