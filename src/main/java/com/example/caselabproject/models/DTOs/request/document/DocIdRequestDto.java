package com.example.caselabproject.models.DTOs.request.document;

import com.example.caselabproject.services.DocumentService;
import com.example.caselabproject.validation.annotations.CheckOrganization;
import jakarta.validation.constraints.Min;
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
public class DocIdRequestDto {
    @CheckOrganization(serviceClass = DocumentService.class)
    @Min(value = 1, message = "id can not be less 1")
    private Long docId;
}
