package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentConstructorTypeIdNotExistsException;
import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
    @Transactional
    public void deleteById(Long id) {
        DocumentConstructorType documentType = typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(id));

        documentType.setRecordState(RecordState.DELETED);
        typeRepository.save(documentType);
    }

    @Override
    @Transactional
    public DocumentConstructorTypeResponseDto getById(Long id) {
        DocumentConstructorType constructorType = typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeIdNotExistsException(
                        404, "Document type with " + id + " id doesn't exist"));

        return DocumentConstructorTypeResponseDto.mapFromEntity(constructorType);
    }

    @Override
    public List<DocumentConstructorTypeResponseDto> getAllContaining(
            String name, RecordState state, Integer page, Integer size) {
        Page<DocumentConstructorType> documentTypes =
                typeRepository.findAllByNameContainingIgnoreCaseAndRecordState(
                        name, state, PageRequest.of(page, size, Sort.by("name").ascending()));

        return documentTypes
                .map(DocumentConstructorTypeResponseDto::mapFromEntity)
                .toList();
    }

    private DocumentConstructorType saveDocumentConstructorType(DocumentConstructorType typeToSave) {
        try {
            return typeRepository.save(typeToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentConstructorTypeNameExistsException(typeToSave.getName());
        }
    }
}
