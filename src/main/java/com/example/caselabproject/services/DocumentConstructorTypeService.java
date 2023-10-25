package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;

import javax.validation.Valid;

public interface DocumentConstructorTypeService {

    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    DocumentConstructorTypeResponseDto renameById(Long id,
                                                  @Valid DocumentConstructorTypeRequestDto typeRequestDto);
}
