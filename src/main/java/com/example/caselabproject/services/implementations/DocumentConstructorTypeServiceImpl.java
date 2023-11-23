package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeAlreadyActiveException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeAlreadyDeletedException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.Field;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.FieldRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentConstructorTypeServiceImpl implements DocumentConstructorTypeService {
    private final DocumentConstructorTypeRepository typeRepository;
    private final FieldRepository fieldRepository;

    @Override
    public DocumentConstructorTypeCreateResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();

        // Сохраняем полученные fields. Сущность Field имеет ограничение уникальности
        // на поле name. При вызове метода saveAll будут сохранены fields, имена
        // которых не заняты, а fields с существующими именами метод не изменит.
        List<Field> fields = fieldRepository.saveAll(typeToSave.getFields());
        typeToSave.setFields(fields);

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

        DocumentConstructorType updated = typeRequestDto.mapToEntity();

        // Найденные поля
        List<Field> foundFields = fieldRepository.findAllByNameIn(
                updated.getFields().stream().map(Field::getName).toList());
        // Результирующий список полей
        List<Field> fields = new ArrayList<>(foundFields);
        // Если найдены не все поля
        if (foundFields.size() < updated.getFields().size()) {
            // Создаем список названий найденных полей
            List<String> foundFieldNames = foundFields.stream().map(Field::getName).toList();
            // Создаем список полей, которые нужно будет сохранить
            List<Field> fieldsToSave = new ArrayList<>();
            for (Field field : updated.getFields()) {
                // Если поля нет в списке найденных
                if (!foundFieldNames.contains(field.getName())) {
                    // Добавляем поле в список для сохранения
                    fieldsToSave.add(field);
                }
            }
            fields.addAll(fieldRepository.saveAll(fieldsToSave));
        }

        // обновляем существующую entity
        typeToUpdate.setName(updated.getName());
        typeToUpdate.setPrefix(updated.getPrefix());
        typeToUpdate.setFields(fields);

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
