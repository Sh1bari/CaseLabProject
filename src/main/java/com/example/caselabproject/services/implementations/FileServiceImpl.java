package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.response.FileDownloadResponseDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.FileRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    @Override
    public List<FileResponseDto> addFile(String username, MultipartFile multipartFile, Long documentId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        File file = new File();

        multipartFileToFile(multipartFile, file);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));
        file.setDocument(document);

        try {
            List<File> files = document.getFiles();
            files.add(file);
            document.setFiles(files);
            document.setUpdateDate(LocalDateTime.now());
            documentRepository.save(document);
        } catch (Exception e) {
            throw new FileConnectToDocumentException();
        }

        return document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList();
    }

    @Override
    public List<FileResponseDto> getFiles(Long documentId, Pageable pageable) {

        Page<File> files = fileRepository.findAllByDocument_Id(documentId, pageable);

        if (files.isEmpty()) {
            throw new NoFilesPageFoundException(pageable.getPageNumber());
        }

        return files.map(FileResponseDto::mapFromEntity).toList();
    }

    @Override
    public FileDownloadResponseDto downloadFile(Long documentId, Long fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotExistException(fileId)
        );

        return FileDownloadResponseDto.mapFromEntity(file);
    }

    @Override
    public List<FileResponseDto> updateFile(String username, MultipartFile file,
                                            Long documentId, Long fileId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        File updateFile = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotExistException(fileId)
        );

        multipartFileToFile(file, updateFile);

        fileRepository.save(updateFile);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        document.setUpdateDate(LocalDateTime.now());

        documentRepository.save(document);

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


    private void multipartFileToFile(MultipartFile multipartFile, File file) {
        try {
            file.setName(multipartFile.getOriginalFilename());
            file.setType(multipartFile.getContentType());
            file.setBytes(multipartFile.getBytes());
            file.setSize(multipartFile.getSize());
            file.setPath("idk");
        } catch (IOException e) {
            throw new FileIsEmptyException(multipartFile.getOriginalFilename());
        }
    }
}
