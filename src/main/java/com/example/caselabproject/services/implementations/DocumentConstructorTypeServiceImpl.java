package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        try {
            DocumentConstructorType createdType = typeRepository.save(typeRequestDto.mapToEntity());
            return DocumentConstructorTypeResponseDto.mapFromEntity(createdType);
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentConstructorTypeNameExistsException(422, "Document type " + typeRequestDto.getName() + " already exists.");
        }
    }


}
