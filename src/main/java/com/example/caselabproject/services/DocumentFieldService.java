package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.document.DocumentFieldUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentFieldService {

    /**
     * Заменяет значения полей в документе и обновляет дату обновления документа.
     * Если какое-то из полей не существовало в документе, оно будет добавлено.
     * Если какое-то из добавляемых полей не существовало в базе данных, оно будет создано.
     */
    @Transactional
    DocumentResponseDto replaceDocumentFields(
            @Min(value = 1L, message = "Id must be >= 1") Long documentId,
            @UniqueElements(message = "Fields must be unique")
            @Size(min = 1, message = "Request doesn't contain fields")
            List<@Valid DocumentFieldUpdateRequestDto> documentFieldDtos);
}
