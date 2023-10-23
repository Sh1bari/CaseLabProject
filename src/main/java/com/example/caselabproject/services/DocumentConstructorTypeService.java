package com.example.caselabproject.services;

import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface DocumentConstructorTypeService {

    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);
}
