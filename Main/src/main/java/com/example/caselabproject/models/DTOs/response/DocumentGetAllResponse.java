package com.example.caselabproject.models.DTOs.response;


import com.example.caselabproject.models.entities.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
public class DocumentGetAllResponse {

    private Long id;

    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private Long creatorId;

    public static DocumentGetAllResponse mapFromEntity(Document document) {

        return DocumentGetAllResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .creatorId(document.getCreator().getId())
                .creationDate(document.getCreationDate())
                .updateDate(document.getUpdateDate())
                .creatorId(document.getCreator().getId())
                .build();
    }

    public static List<DocumentGetAllResponse> mapFromListOfEntities(List<Document> document) {
        List<DocumentGetAllResponse> res = new ArrayList<>();
        document.forEach(o -> {
            res.add(DocumentGetAllResponse.builder()
                    .id(o.getId())
                    .name(o.getName())
                    .updateDate(o.getUpdateDate())
                    .creatorId(o.getCreator().getId())
                    .creationDate(o.getCreationDate())
                    .build());
        });
        return res;
    }
}
