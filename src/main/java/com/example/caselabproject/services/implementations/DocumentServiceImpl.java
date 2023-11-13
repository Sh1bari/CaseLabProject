package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DocumentRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.models.entities.Document;
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
import java.util.List;

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

        document.setDocumentConstructorType(documentConstructorTypeRepository
                .findByName(request.getConstructorTypeName()).orElseThrow(
                        () -> new DocumentConstructorTypeNameNotFoundException(request.getConstructorTypeName())
                ));

        try {
            documentRepository.save(document);
        } catch (Exception e) {
            throw new DocumentCreateException(document.getName());
        }

        return DocumentCreateResponseDto.mapFromEntity(document);
    }

    @Override
    public DocumentResponseDto findDocument(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

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

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document updateDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        if (updateDocument.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        updateDocument.setUpdateDate(request.mapToEntity().getUpdateDate());
        updateDocument.setName(request.getName());
        updateDocument.setDocumentConstructorType(documentConstructorTypeRepository
                .findByName(request.getConstructorTypeName()).orElseThrow(
                        () -> new DocumentConstructorTypeNameNotFoundException(request.getConstructorTypeName())
                ));

        documentRepository.save(updateDocument);

        return DocumentResponseDto.mapFromEntity(updateDocument);
    }

    @Override
    public boolean deleteDocument(String username, Long documentId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        if (document.getRecordState().equals(RecordState.DELETED)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        document.setRecordState(RecordState.DELETED);
        documentRepository.save(document);

        return true;
    }
}
