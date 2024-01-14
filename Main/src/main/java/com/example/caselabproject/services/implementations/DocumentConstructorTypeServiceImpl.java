package com.example.caselabproject.services.implementations;


import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeAlreadyActiveException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeAlreadyDeletedException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.Field;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.FieldRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    
    @Override
    public DocumentConstructorTypeCreateResponseDto create(DocumentConstructorTypeRequestDto typeRequestDto) {
        DocumentConstructorType typeToSave = typeRequestDto.mapToEntity();

        List<Field> fields = saveFieldsInternal(typeToSave.getFields());
        
        typeToSave.setFields(fields);
        typeToSave.setOrganization(findAssociatedOrganizationInternal());
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

        List<Field> fields = saveFieldsInternal(updated.getFields());

        // обновляем существующую entity
        typeToUpdate.setName(updated.getName());
        typeToUpdate.setPrefix(updated.getPrefix());
        typeToUpdate.setFields(fields);

        return DocumentConstructorTypeUpdateResponseDto.mapFromEntity(
                saveInternal(typeToUpdate));
    }

    /**
     * Метод сохраняет поля, которые еще не существуют и находит поля, которые уже существуют
     *
     * @param fields список полей
     * @return общий список найденных и сохраненных полей
     */
    @NotNull
    private List<Field> saveFieldsInternal(List<Field> fields) {
        // Найденные поля
        List<Field> foundFields = fieldRepository.findAllByNameIn(
                fields.stream().map(Field::getName).toList());
        // Результирующий список полей
        List<Field> result = new ArrayList<>(foundFields);
        // Если найдены не все поля
        if (foundFields.size() < fields.size()) {
            // Создаем список названий найденных полей
            List<String> foundFieldNames = foundFields.stream().map(Field::getName).toList();
            // Создаем список полей, которые нужно будет сохранить
            List<Field> fieldsToSave = new ArrayList<>();
            for (Field field : fields) {
                // Если поля нет в списке найденных
                if (!foundFieldNames.contains(field.getName())) {
                    // Добавляем поле в список для сохранения
                    fieldsToSave.add(field);
                }
            }
            result.addAll(fieldRepository.saveAll(fieldsToSave));
        }
        return result;
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
        Long organizationId = findAssociatedOrganizationInternal().getId();
        
        Page<DocumentConstructorType> documentTypes =
                typeRepository.findAllByNameContainingIgnoreCaseAndRecordStateAndOrganizationId(
                        name, state, organizationId,
                        PageRequest.of(page, size, Sort.by("name").ascending()));
        
        return documentTypes
                .map(DocumentConstructorTypeByIdResponseDto::mapFromEntity)
                .toList();
    }
    
    @Override
    public Long getOrganizationIdByEntityId(Long entityId) {
        return findByIdInternal(entityId).getOrganization().getId();
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
    
    private Organization findAssociatedOrganizationInternal() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username))
                .getOrganization();
    }
}
