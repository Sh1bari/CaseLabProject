package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    @Override
    public DocumentResponseDto createDocument(String username, DocumentCreateRequestDto request) {

        Document document = request.mapToEntity();
        document.setRecordState(RecordState.ACTIVE);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserByPrincipalUsernameDoesNotExistException(username));
        document.setCreator(user);


        try {
            documentRepository.save(document);
        } catch (Exception e) {
            throw new DocumentCreateException();
        }

        return DocumentResponseDto.mapFromEntity(document);
    }

    @Override
    public DocumentResponseDto findDocument(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        return DocumentResponseDto.mapFromEntity(document);
    }

    @Override
    public List<DocumentResponseDto> filteredDocument(Integer page, String name) {

        int limit = 20;

        Page<Document> documents;

        if (!name.isEmpty()) {
            documents = documentRepository
                    .findAllByNameContainingIgnoreCase(name,
                            PageRequest.of(page, limit, Sort.by("name").ascending()));
        } else {
            documents = documentRepository.findAll(PageRequest.of(page, limit));
        }

        if (documents.isEmpty()) {
            throw new NoDocumentPageFoundException(page);
        }

        return documents.map(DocumentResponseDto::mapFromEntity).toList();
    }

    @Override
    public DocumentResponseDto updateDocument(String username, DocumentUpdateRequestDto request, Long documentId) {

        if (!documentRepository.existsById(documentId)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document updateDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        updateDocument.setName(request.getName());
        updateDocument.setUpdateDate(request.mapToEntity().getUpdateDate());
        documentRepository.save(updateDocument);

        return DocumentResponseDto.mapFromEntity(updateDocument);
    }

    @Override
    public void deleteDocument(String username, Long documentId) {

        if (!documentRepository.existsById(documentId)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        documentRepository.deleteById(documentId);
    }
}
