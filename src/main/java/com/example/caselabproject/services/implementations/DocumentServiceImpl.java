package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentCreateException;
import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.repositories.DocumentRepo;
import com.example.caselabproject.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepo documentRepo;
    @Override
    public DocumentCreateResponseDto createDocument(DocumentCreateRequestDto request) {
        Document document = request.mapToEntity();
        try {
            documentRepo.save(document);
        }catch (Exception e){
            throw new DocumentCreateException(500, "Can not create document!");
        }
        return DocumentCreateResponseDto.mapFromEntity(document);
    }
}
