package com.example.caselabproject.models.DTOs.request;

import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocIdRequestDto {
    @Min(value = 1, message = "id can not be less 1")
    private Long docId;
}
