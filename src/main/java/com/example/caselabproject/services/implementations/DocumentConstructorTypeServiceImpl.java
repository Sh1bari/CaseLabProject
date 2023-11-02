package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;
    private final DocumentRepository documentRepository;

    @Override
    public DocumentConstructorTypeCreateResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();
        typeToSave.getFields().forEach(field -> field.setDocumentConstructorType(typeToSave));
        typeToSave.setRecordState(RecordState.ACTIVE);
        return DocumentConstructorTypeCreateResponseDto
                .mapFromEntity(saveInternal(typeToSave));
    }

    @Override
    public DocumentConstructorTypeUpdateResponseDto renameById(Long id,
                                                               DocumentConstructorTypePatchRequestDto typeRequestDto) {
        DocumentConstructorType documentType = findByIdInternal(id);
        documentType.setName(typeRequestDto.getName());
        documentType = saveInternal(documentType);
        return DocumentConstructorTypeUpdateResponseDto.mapFromEntity(documentType);
    }

    @Override
    public DocumentConstructorTypeUpdateResponseDto updateById(
            Long id, DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToUpdate = findByIdInternal(id);

        // проверяем, что тип, который нужно обновить, не используется в документах
        if (documentRepository.existsByDocumentConstructorType(typeToUpdate)) {
            //если используется
            throw new DocumentConstructorTypeHasAssociatedDocumentsException(id);
        } else {
            // если не используется:

            // удаляем старую версию
            typeRepository.deleteById(id);
            typeRepository.flush();

            // сохраняем новую версию
            DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();
            typeToSave.setRecordState(RecordState.ACTIVE);
            typeToSave.getFields().forEach(field -> field.setDocumentConstructorType(typeToSave));
            return DocumentConstructorTypeUpdateResponseDto
                    .mapFromEntity(saveInternal(typeToSave));
        }
    }

    @Override
    public void deleteById(Long id) {
        DocumentConstructorType documentType = typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(id));
        if (documentType.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentConstructorTypeAlreadyDeletedException(id);
        }
        documentType.setRecordState(RecordState.DELETED);
        typeRepository.save(documentType);
    }

    @Override
    public DocumentConstructorTypeRecoverResponseDto recoverById(Long id) {
        DocumentConstructorType documentType = typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(id));
        if (documentType.getRecordState().equals(RecordState.ACTIVE)) {
            throw new DocumentConstructorTypeAlreadyActiveException(id);
        }
        documentType.setRecordState(RecordState.ACTIVE);
        DocumentConstructorType res = typeRepository.save(documentType);
        return DocumentConstructorTypeRecoverResponseDto.mapFromEntity(res);
    }

    @Override
    public DocumentConstructorTypeByIdResponseDto getById(Long id) {
        DocumentConstructorType constructorType = findByIdInternal(id);
        return DocumentConstructorTypeByIdResponseDto.mapFromEntity(constructorType);
    }

    @Override
    public List<DocumentConstructorTypeByIdResponseDto> getAllContaining(String name,
                                                                         RecordState state,
                                                                         Integer page,
                                                                         Integer size) {
        Page<DocumentConstructorType> documentTypes =
                typeRepository.findAllByNameContainingIgnoreCaseAndRecordState(name, state,
                        PageRequest.of(page, size, Sort.by("name").ascending()));
        if (documentTypes.getSize() == 0) {
            throw new DocumentConstructorTypePageNotFoundException(page);
        }
        return documentTypes
                .map(DocumentConstructorTypeByIdResponseDto::mapFromEntity)
                .toList();
    }

    /**
     * Внутренний метод, позволяющий сохранить DocumentConstructor. Используется
     * для избежания повторов кода.
     */
    private DocumentConstructorType saveInternal(DocumentConstructorType typeToSave) {
        try {
            return typeRepository.save(typeToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DocumentConstructorTypeNameExistsException(typeToSave.getName());
        }
    }

    /**
     * Внутренний метод, позволяющий найти DocumentConstructor по id. Используется
     * для избежания повторов кода.
     */
    private DocumentConstructorType findByIdInternal(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(id));
    }
}
