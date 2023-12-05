package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentSecurityService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    public boolean canUpdateDocument(String username, Long documentId) {
        return userRepository.existsByUsernameAndDocuments_id(username, documentId);
    }
}
