package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

public interface DocumentConstructorTypeService {

    @Transactional
    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    @Transactional
    DocumentConstructorTypeResponseDto updateById(@Min(1L) Long id,
                                                  @Valid DocumentConstructorTypePatchRequestDto typeRequestDto);

    @Transactional
    void deleteById(@Min(1L) Long id);
}
