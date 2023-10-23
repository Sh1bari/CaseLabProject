package com.example.caselabproject.services;

import com.example.caselabproject.dtos.DocumentConstructorTypeDto;
import com.example.caselabproject.dtos.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.dtos.response.DocumentConstructorTypeResponseDto;

import javax.validation.Valid;
import java.util.Optional;

public interface DocumentConstructorTypeService {

    /**
     * Returns {@link DocumentConstructorTypeResponseDto} based on provided <code>typeRequestDto</code>.
     */
    DocumentConstructorTypeResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    /**
     * Returns {@link Optional} of {@link DocumentConstructorTypeDto} based on provided <code>id</code>.
     */
    Optional<DocumentConstructorTypeDto> findById(Long id); // should be here @Valid?
}
