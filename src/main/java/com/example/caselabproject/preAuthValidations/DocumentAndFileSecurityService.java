package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentAndFileSecurityService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    public boolean canAddFileToDocument(String username, Long documentId) {
        return userRepository.existsByUsernameAndDocuments_id(username, documentId)
                && documentRepository.existsByIdAndRecordState(documentId, RecordState.ACTIVE);
    }

    public boolean canUpdateDocumentOrFile(String username, Long documentId) {
        return userRepository.existsByUsernameAndDocuments_id(username, documentId)
                && documentRepository.existsByIdAndRecordState(documentId, RecordState.ACTIVE);
    }

    public boolean canDeleteDocumentOrFile(String username, Long documentId) {
        return userRepository.existsByUsernameAndDocuments_id(username, documentId)
                && documentRepository.existsByIdAndRecordState(documentId, RecordState.ACTIVE);
    }
}
