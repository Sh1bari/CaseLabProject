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

        // Получаем имена полей из dto, получаем из бд существующие поля и создаем недостающие
        List<Field> fields = fieldRepository.saveAll(
                documentFieldDtos.stream().map(
                        documentFieldDto -> documentFieldDto.getField().mapToEntity()
                ).toList()
        );
        // Создаем Map полей и их значений
        Map<Field, String> fieldsMap = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            fieldsMap.put(fields.get(i), documentFieldDtos.get(i).getValue());
        }
        document.setFieldsValues(fieldsMap);

        return DocumentResponseDto.mapFromEntity(
                documentRepository.save(document));
    }

    private Document findDocumentByIdInternal(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));
    }
}
