package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.DocumentTypeIdNotExistsException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.w3c.dom.DocumentType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;

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

    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto renameById(Long id,
                                                         DocumentConstructorTypeRequestDto typeRequestDto) {
        final Optional<DocumentConstructorType> optionalDocumentType = typeRepository.findById(id);
        final DocumentConstructorType documentType = optionalDocumentType.orElseThrow(
                () -> new DocumentTypeIdNotExistsException(404, "Document type with " + id + " id doesn't exist"));

        documentType.setName(typeRequestDto.getName());
        typeRepository.save(documentType);

        return DocumentConstructorTypeResponseDto.mapFromEntity(documentType);
    }
}
