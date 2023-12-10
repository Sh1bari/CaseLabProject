package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.document.DocumentAccessException;
import com.example.caselabproject.exceptions.document.DocumentCreateException;
import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.document.NoDocumentPageFoundException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.exceptions.user.UserByPrincipalUsernameDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.document.DocumentRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.Field;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.DocumentPageRepository;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentPageRepository documentPageRepository;

    private final DocumentConstructorTypeRepository documentConstructorTypeRepository;

    private final UserRepository userRepository;

    @Override
    public DocumentCreateResponseDto createDocument(String username, DocumentRequestDto request) {

        Document document = request.mapToEntity();

        document.setRecordState(RecordState.ACTIVE);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserByPrincipalUsernameDoesNotExistException(username));
        document.setCreator(user);

        DocumentConstructorType constructorType = documentConstructorTypeRepository
                .findById(request.getConstructorTypeId())
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(request.getConstructorTypeId()));
        document.setDocumentConstructorType(constructorType);

        // Заполняем поля документа пустыми строками
        Map<Field, String> fieldsValues = new HashMap<>();
        constructorType.getFields()
                .forEach(field -> fieldsValues.put(field, ""));
        document.setFieldsValues(fieldsValues);

        try {
            documentRepository.save(document);
        } catch (Exception e) {
            throw new DocumentCreateException(document.getName());
        }

        return DocumentCreateResponseDto.mapFromEntity(document);
    }

    @Override
    public DocumentResponseDto findDocument(Long documentId) {

        Document document = findDocumentByIdInternal(documentId);

        if (document.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        return DocumentResponseDto.mapFromEntity(document);
    }

    @Override
    public List<DocumentResponseDto> filteredDocument(Pageable pageable,
                                                      String name,
                                                      RecordState state,
                                                      LocalDateTime start,
                                                      LocalDateTime end) {

        Page<Document> documents;

        if (!(name.length() == 0)) {
            documents = documentPageRepository
                    .findAllByNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
                            name, pageable, start, end, state);
        } else {
            documents = documentPageRepository.findAllByCreationDateAfterAndCreationDateBeforeAndRecordState(
                    pageable, start, end, state);
        }

        if (documents.isEmpty()) {
            throw new NoDocumentPageFoundException(pageable.getPageNumber());
        }

        return documents.map(DocumentResponseDto::mapFromEntity).toList();
    }


    @Override
    public DocumentResponseDto updateDocument(String username, DocumentRequestDto request, Long documentId) {

        if (!documentRepository.existsById(documentId)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document updateDocument = findDocumentByIdInternal(documentId);

        updateDocument.setUpdateDate(request.mapToEntity().getUpdateDate());
        updateDocument.setName(request.getName());
        updateDocument.setDocumentConstructorType(documentConstructorTypeRepository
                .findById(request.getConstructorTypeId()).orElseThrow(
                        () -> new DocumentConstructorTypeNotFoundException(request.getConstructorTypeId())
                ));

        documentRepository.save(updateDocument);

        return DocumentResponseDto.mapFromEntity(updateDocument);
    }

    @Override
    public boolean deleteDocument(String username, Long documentId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document document = findDocumentByIdInternal(documentId);

        if (document.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        document.setRecordState(RecordState.DELETED);
        documentRepository.save(document);

        return true;
    }

    @Override
    public Long getOrganizationIdByEntityId(Long entityId) {
        return findDocumentByIdInternal(entityId)
                .getOrganization().getId();
    }

    private Document findDocumentByIdInternal(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));
    }
}
