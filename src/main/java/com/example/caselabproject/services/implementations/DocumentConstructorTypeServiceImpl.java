package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.Field;
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
    public DocumentConstructorTypeUpdateResponseDto updateNameAndPrefix(
            Long id,
            DocumentConstructorTypePatchRequestDto patch) {
        DocumentConstructorType documentType = findByIdInternal(id);

        documentType.setName(patch.getName());
        documentType.setPrefix(patch.getPrefix());

        return DocumentConstructorTypeUpdateResponseDto.mapFromEntity(
                saveInternal(documentType));
    }

    @Override
    public DocumentConstructorTypeUpdateResponseDto update(
            Long id, DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToUpdate = findByIdInternal(id);

        // проверяем, что тип, который нужно обновить, не используется в документах
        if (documentRepository.existsByDocumentConstructorType(typeToUpdate)) {
            // если используется
            throw new DocumentConstructorTypeHasAssociatedDocumentsException(id);
        }

        // преобразуем DTO с обновленными полями в entity
        DocumentConstructorType updated = typeRequestDto.mapToEntity();
        // устанавливаем ссылку на обновляемую сущность в каждом поле
        updated.getFields().forEach(field -> field.setDocumentConstructorType(typeToUpdate));

        // обновляем существующую entity
        typeToUpdate.setName(updated.getName());
        typeToUpdate.setPrefix(updated.getPrefix());
        // Обновляем поля. Мы не можем просто использовать typeToUpdate.setFields(),
        // так как List, в котором в Hibernate хранятся связанные сущности Field - Immutable.
        List<Field> fields = typeToUpdate.getFields();
        fields.clear();
        fields.addAll(updated.getFields());

        return DocumentConstructorTypeUpdateResponseDto.mapFromEntity(
                saveInternal(typeToUpdate));
    }

    @Override
    public void deleteById(Long id) {
        DocumentConstructorType typeToDelete = findByIdInternal(id);

        if (typeToDelete.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentConstructorTypeAlreadyDeletedException(id);
        }

        typeToDelete.setRecordState(RecordState.DELETED);
        saveInternal(typeToDelete);
    }

    @Override
    public DocumentConstructorTypeRecoverResponseDto recoverById(Long id) {
        DocumentConstructorType typeToRecover = findByIdInternal(id);

        if (typeToRecover.getRecordState().equals(RecordState.ACTIVE)) {
            throw new DocumentConstructorTypeAlreadyActiveException(id);
        }

        typeToRecover.setRecordState(RecordState.ACTIVE);
        return DocumentConstructorTypeRecoverResponseDto.mapFromEntity(
                saveInternal(typeToRecover));
    }

    @Override
    public DocumentConstructorTypeByIdResponseDto getById(Long id) {
        return DocumentConstructorTypeByIdResponseDto.mapFromEntity(
                findByIdInternal(id));
    }

    @Override
    public List<DocumentConstructorTypeByIdResponseDto> getAllContaining(String name,
                                                                         RecordState state,
                                                                         Integer page,
                                                                         Integer size) {
        Page<DocumentConstructorType> documentTypes =
                typeRepository.findAllByNameContainingIgnoreCaseAndRecordState(name, state,
                        PageRequest.of(page, size, Sort.by("name").ascending()));

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
            // при использовании просто save(), мы не сможем обработать ограничение
            // уникальности, поэтому используем saveAndFlush().
            return typeRepository.saveAndFlush(typeToSave);
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
