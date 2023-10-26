package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

public interface DocumentConstructorTypeService {

    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    DocumentConstructorTypeResponseDto updateById(@Min(1L) Long id,
                                                  @Valid DocumentConstructorTypePatchRequestDto typeRequestDto);

    void deleteById(@Min(1L) Long id);
}
