package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.DocumentFieldUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.Field;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.FieldRepository;
import com.example.caselabproject.services.DocumentFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentFieldServiceImpl implements DocumentFieldService {

    private final DocumentRepository documentRepository;
    private final FieldRepository fieldRepository;

    @Override
    public DocumentResponseDto replaceDocumentFields(Long documentId,
                                                     List<DocumentFieldUpdateRequestDto> documentFieldDtos) {
        Document document = findDocumentByIdInternal(documentId);

        // Создаем список полей, которые нужно будет сохранить, так как их нет в бд
        List<Field> fieldsToSave = new ArrayList<>();
        Map<Field, String> fieldsValues = new HashMap<>();
        for (DocumentFieldUpdateRequestDto documentFieldDto : documentFieldDtos) {
            // Находим поле в бд по его названию
            Field field = fieldRepository.findByNameEquals(documentFieldDto.getField().getName())
                    .orElse(documentFieldDto.getField().mapToEntity());
            // Если такого поля нет, то добавляем его в список для сохранения
            if (field.getId() == null) {
                fieldsToSave.add(field);
            }
            fieldsValues.put(
                    field,
                    documentFieldDto.getValue()
            );
        }
        fieldRepository.saveAll(fieldsToSave);

        document.setFieldsValues(fieldsValues);

        return DocumentResponseDto.mapFromEntity(
                documentRepository.save(document));
    }

    private Document findDocumentByIdInternal(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));
    }
}
