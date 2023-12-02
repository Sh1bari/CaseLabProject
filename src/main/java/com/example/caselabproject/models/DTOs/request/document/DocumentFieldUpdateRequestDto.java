package com.example.caselabproject.models.DTOs.request.document;

import com.example.caselabproject.models.DTOs.request.field.FieldRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Данный класс используется для изменения значения поля
 * документа. При создании же документа, его поля инициализируются
 * пустой строкой (сами поля берутся из типа документа).
 */
@Data
public class DocumentFieldUpdateRequestDto {

    @Valid
    private FieldRequestDto field;

    @NotNull(message = "Value must not be null")
    private String value;
}
