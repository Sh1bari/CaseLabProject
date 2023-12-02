package com.example.caselabproject.models.DTOs.response.application;

import com.example.caselabproject.models.DTOs.ApplicationItemDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApplicationFindResponseDto {
    private Long id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime deadlineDate;
    private LocalDateTime resultDate;
    private Long creatorId;
    private Long documentId;
    private ApplicationStatus applicationStatus;
    private List<ApplicationItemDto> applicationItems;


    public static ApplicationFindResponseDto mapFromEntity(Application application) {
        List<ApplicationItemDto> applicationItemDto = new ArrayList<>();
        application.getApplicationItems().forEach(o -> {
            applicationItemDto.add(ApplicationItemDto.builder()
                    .id(o.getId())
                    .build());
        });
        Long documentId = null;
        try {
            documentId = application.getDocument().getId();
        } catch (Exception e) {

        }
        return ApplicationFindResponseDto.builder()
                .id(application.getId())
                .name(application.getName())
                .documentId(documentId)
                .applicationStatus(application.getApplicationStatus())
                .resultDate(application.getResultDate())
                .creationDate(application.getCreationDate())
                .deadlineDate(application.getDeadlineDate())
                .creatorId(application.getCreatorId().getId())
                .applicationItems(applicationItemDto)
                .build();
    }

    public static List<ApplicationFindResponseDto> mapFromListEntity(List<Application> applications) {
        List<ApplicationFindResponseDto> res = new ArrayList<>();
        applications.forEach(o -> {
            Long documentId = null;
            Document document = o.getDocument();
            if (document != null) {
                documentId = document.getId();
            }
            List<ApplicationItemDto> applicationItemDtos = new ArrayList<>();
            for (ApplicationItem applicationItem : o.getApplicationItems()) {
                applicationItemDtos.add(ApplicationItemDto.builder()
                        .id(applicationItem.getId())
                        .build());
            }
            res.add(ApplicationFindResponseDto.builder()
                    .id(o.getId())
                    .name(o.getName())
                    .documentId(documentId)
                    .creationDate(o.getCreationDate())
                    .deadlineDate(o.getDeadlineDate())
                    .resultDate(o.getResultDate())
                    .applicationStatus(o.getApplicationStatus())
                    .creatorId(o.getCreatorId().getId())
                    .applicationItems(applicationItemDtos)
                    .build()
            );
        });
        return res;
    }
}
