package com.example.caselabproject.models.DTOs.request.application;


import com.example.caselabproject.models.enums.ApplicationItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationItemVoteRequestDto {
    @NotNull(message = "Vote status cant be null")
    private ApplicationItemStatus status;
    private String comment;
}
