package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {

    private final DocumentConstructorTypeRepository typeRepository;

    @Override
    public DocumentConstructorTypeResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();
        typeToSave.getFields().forEach(field -> field.setDocumentConstructorType(typeToSave));
        return DocumentConstructorTypeResponseDto
                .mapFromEntity(saveDocumentConstructorType(typeToSave));
    }

    @Override
    public DocumentConstructorTypeResponseDto updateById(Long id, DocumentConstructorTypePatchRequestDto typeRequestDto) {
        DocumentConstructorType documentType = typeRepository.findById(id).orElseThrow(
                () -> new DocumentConstructorTypeNotFoundException(id));

        documentType.setName(typeRequestDto.getName());
        documentType = saveDocumentConstructorType(documentType);

        return DocumentConstructorTypeResponseDto.mapFromEntity(documentType);
    }

    @Override
    public void deleteById(Long id) {
        DocumentConstructorType documentType = typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(id));

        documentType.setRecordState(RecordState.DELETED);
        typeRepository.save(documentType);
    }

    private DocumentConstructorType saveDocumentConstructorType(DocumentConstructorType typeToSave) {
        try {
            return typeRepository.save(typeToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentConstructorTypeNameExistsException(typeToSave.getName());
        }
    }
}
