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
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
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

        try {
            FileUtils.writeByteArrayToFile(
                    new java.io.File("src\\main\\resources\\files\\"
                            + multipartFile.getOriginalFilename()),
                    multipartFile.getBytes());
            multipartFileToFile(multipartFile, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    public FileDownloadResponseDto downloadFile(Long documentId, Long fileId) throws IOException {

        File file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotExistException(fileId)
        );

        java.io.File downloadFile = new java.io.File(file.getPath());


        FileDownloadResponseDto responseDto = FileDownloadResponseDto.mapFromEntity(file);

        responseDto.setBytes(FileUtils.readFileToByteArray(downloadFile));

        return responseDto;
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

        try {
            FileUtils.writeByteArrayToFile(new java.io.File(
                            "src/main/resources/files/" + file.getOriginalFilename()),
                    file.getBytes());
            multipartFileToFile(file, updateFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotExistException(fileId));

        List<File> files = document.getFiles();

        files.remove(file);
        document.setFiles(files);
        document.setUpdateDate(LocalDateTime.now());
        documentRepository.save(document);

        return document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList();
    }


    private void multipartFileToFile(MultipartFile multipartFile, File file) {
        file.setName(multipartFile.getOriginalFilename());
        file.setType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setPath("src\\main\\resources\\files\\" + multipartFile.getOriginalFilename());
    }
}
