package com.example.caselabproject.services;

import com.example.caselabproject.dtos.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.dtos.response.DocumentConstructorTypeResponseDto;

import javax.validation.Valid;

public interface DocumentConstructorTypeService {

    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);
}
