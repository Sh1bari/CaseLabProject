package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import org.springframework.transaction.annotation.Transactional;
import com.example.caselabproject.models.enums.RecordState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DocumentConstructorTypeService {

    @Transactional
    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    @Transactional
    DocumentConstructorTypeResponseDto updateById(@Min(1L) Long id,
                                                  @Valid DocumentConstructorTypePatchRequestDto typeRequestDto);

    @Transactional
    void deleteById(@Min(1L) Long id);

    DocumentConstructorTypeResponseDto getById(@Min(1L) Long id);

    List<DocumentConstructorTypeResponseDto> getAllContaining(
            @NotNull String name, @NotNull RecordState state,
            @Min(0) Integer page, @Min(1) Integer size);
}
