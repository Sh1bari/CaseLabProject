package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentConstructorTypeService {

    @Transactional
    DocumentConstructorTypeCreateResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    @Transactional
    DocumentConstructorTypeUpdateResponseDto updateById(@Min(value = 1L, message = "id can't be less than 1") Long id,
                                                        @Valid DocumentConstructorTypePatchRequestDto typeRequestDto);

    @Transactional
    void deleteById(@Min(value = 1L, message = "id can't be less than 1") Long id);
    @Transactional
    DocumentConstructorTypeRecoverResponseDto recoverById(@Min(value = 1L, message = "id can't be less than 1") Long id);

    @Transactional
    DocumentConstructorTypeByIdResponseDto getById(@Min(value = 1L, message = "id can't be less than 1") Long id);

    @Transactional
    List<DocumentConstructorTypeByIdResponseDto> getAllContaining(
            @NotNull(message = "name must not be null and must contain at least one non-whitespace character.") String name,
            @NotNull(message = "state must not be null.") RecordState state,
            @Min(value = 0L, message = "page can't be less than 0") Integer page,
            @Min(value = 1L, message = "size can't be less than 1") Integer size);
}
