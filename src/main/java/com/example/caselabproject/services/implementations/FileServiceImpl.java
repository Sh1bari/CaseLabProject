package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentAccessException;
import com.example.caselabproject.exceptions.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.FileAddException;
import com.example.caselabproject.exceptions.FileNotExistException;
import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.FileRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    @Override
    public List<FileResponseDto> addFile(String username, FileCreateRequestDto request, Long documentId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        File file = request.mapToEntity();
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));
        file.setDocument(document);

        try {
            List<File> files = document.getFiles();
            files.add(file);
            document.setFiles(files);
            fileRepository.save(file);
        } catch (Exception e) {
            throw new FileAddException();
        }


        return document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList();
    }

    @Override
    public List<FileResponseDto> deleteFile(String username, Long documentId, Long fileId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        if (!fileRepository.existsById(fileId)) {
            throw new FileNotExistException(fileId);
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        fileRepository.deleteById(fileId);

        return document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList();
    }


}
