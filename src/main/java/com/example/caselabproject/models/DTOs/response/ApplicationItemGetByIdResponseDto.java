package com.example.caselabproject.models.DTOs.response;

import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationItemGetByIdResponseDto {
    private Long id;
    private LocalDateTime voteTime;
    private LocalDateTime createTime;
    private ApplicationItemStatus status;
    private RecordState recordState;
    private String comment;
    private Long applicationId;
    private Long toDepartmentId;
    private Long toUserId;

    public static ApplicationItemGetByIdResponseDto mapFromEntity(ApplicationItem applicationItem) {
        Long toUserId = null;
        try {
            toUserId = applicationItem.getToUser().getId();
        } catch (Exception e) {
        }
        return ApplicationItemGetByIdResponseDto.builder()
                .id(applicationItem.getId())
                .voteTime(applicationItem.getVoteTime())
                .createTime(applicationItem.getCreateTime())
                .status(applicationItem.getStatus())
                .recordState(applicationItem.getRecordState())
                .comment(applicationItem.getComment())
                .applicationId(applicationItem.getApplication().getId())
                .toDepartmentId(applicationItem.getToDepartment().getId())
                .toUserId(toUserId)
                .build();
    }

}
