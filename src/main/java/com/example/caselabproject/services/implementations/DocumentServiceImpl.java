package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentCreateException;
import com.example.caselabproject.exceptions.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.NoDocumentPageFoundException;
import com.example.caselabproject.exceptions.UserByPrincipalUsernameDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentFindResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentUpdateResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepo;

    private final Integer limit = 20;

    @Override
    public DocumentCreateResponseDto createDocument(String username, DocumentCreateRequestDto request) {

        Document document = request.mapToEntity();
        document.setRecordState(RecordState.ACTIVE);
        User user = userRepo.findByUsernameAndRecordState(username, RecordState.ACTIVE)
                .orElseThrow(() -> new UserByPrincipalUsernameDoesNotExistException(username));
        document.setCreator(user);


        try {
            documentRepository.save(document);
        } catch (Exception e) {
            throw new DocumentCreateException();
        }

        return DocumentCreateResponseDto.mapFromEntity(document);
    }

    @Override
    public DocumentFindResponseDto findDocument(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        return DocumentFindResponseDto.mapFromEntity(document);
    }

    @Override
    public List<Document> filteredDocument(Integer page) {

        Pageable pageable = PageRequest.of(page, limit);
        List<Document> documentList = documentRepository.findAll(pageable).getContent();
        if(documentList.isEmpty()){
            throw new NoDocumentPageFoundException(page);
        }else return documentList;
    }

    @Override
    public DocumentUpdateResponseDto updateDocument(String username, DocumentUpdateRequestDto request, Long id) {

        /*if (!documentRepository.existsById(document.getId())) {
            throw new DocumentDoesNotExistException(document.getId());
        }

        Document updateDocument = documentRepository.findById(document.getId())
                .orElseThrow(() -> new DocumentDoesNotExistException(document.getId()));

        updateDocument.setName(document.getName());
        updateDocument.setUpdateDate(document.getUpdateDate());
        documentRepository.save(updateDocument);

        return DocumentUpdateResponseDto.mapFromEntity(document);*/
        return null;
    }

    @Override
    public boolean deleteDocument(Long documentId) {

        /*if (!documentRepository.existsById(documentId)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        documentRepository.deleteById(documentId);*/
        return false;
    }
}
