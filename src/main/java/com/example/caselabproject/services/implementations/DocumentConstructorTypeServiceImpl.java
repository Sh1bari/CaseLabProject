package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.DocumentConstructorTypeIdNotExistsException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;

    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();
        typeToSave.getFields().forEach(field -> field.setDocumentConstructorType(typeToSave));
        return DocumentConstructorTypeResponseDto
                .mapFromEntity(this.saveInternal(typeToSave));
    }

    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto updateById(Long id,
                                                         DocumentConstructorTypePatchRequestDto typeRequestDto) {
        final Optional<DocumentConstructorType> optionalDocumentType = typeRepository.findById(id);
        DocumentConstructorType documentType = optionalDocumentType.orElseThrow(
                () -> new DocumentConstructorTypeIdNotExistsException(404, "Document type with " + id + " id doesn't exist"));

        documentType.setName(typeRequestDto.getName());
        documentType = this.saveInternal(documentType);

        return DocumentConstructorTypeResponseDto.mapFromEntity(documentType);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        typeRepository.findById(id)
                .ifPresent((documentType) -> {
                    documentType.setRecordState(RecordState.DELETED);
                    typeRepository.save(documentType);
                });
    }

    private DocumentConstructorType saveInternal(DocumentConstructorType typeToSave) {
        try {
            return typeRepository.save(typeToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentConstructorTypeNameExistsException(422,
                    "Document type " + typeToSave.getName() + " already exists.");
        }
    }
}
