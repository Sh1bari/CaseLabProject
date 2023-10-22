package com.example.caselabproject.services.implementations;

import com.example.caselabproject.dtos.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.dtos.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;


    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        return DocumentConstructorTypeResponseDto.mapFromEntity(typeRepository.save(typeRequestDto.mapToEntity()));
    }
}
