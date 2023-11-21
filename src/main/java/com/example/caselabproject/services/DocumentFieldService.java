package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentFieldUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentFieldService {

    /**
     * Заменяет значения полей в документе. Если какое-то из полей не существовало
     * в документе, оно будет добавлено. Если какое-то из добавляемых полей не
     * существовало в базе данных, оно будет создано.
     */
    @Transactional
    DocumentResponseDto replaceDocumentFields(
            @Min(value = 1L, message = "Id must be >= 1") Long documentId,
            @Size(min = 1, message = "Request doesn't contain fields, nothing to put")
            List<@Valid DocumentFieldUpdateRequestDto> fields);
}
