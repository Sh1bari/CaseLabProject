package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.document.DocumentAccessException;
import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.file.FileConnectToDocumentException;
import com.example.caselabproject.exceptions.file.FileNotExistException;
import com.example.caselabproject.exceptions.file.NoFilesPageFoundException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public Page<FileResponseDto> addFile(String username, MultipartFile multipartFile, Long documentId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        File file = new File();

        file.setDocument(document);

        try {
            multipartFileToFile(multipartFile, file);
            FileUtils.writeByteArrayToFile(
                    new java.io.File(file.getPath()),
                    multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<File> files = document.getFiles();

        // Эта строка нужна для того, чтобы в выводе списка файлов не было дубликтов
        files.size();

        files.add(file);
        document.setFiles(files);
        document.setUpdateDate(LocalDateTime.now());

        try {
            documentRepository.save(document);
        } catch (Exception e) {
            throw new FileConnectToDocumentException();
        }

        return new PageImpl<>(document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList());
    }

    @Override
    public Page<FileResponseDto> getFiles(Long documentId, Pageable pageable) {

        Page<File> files = fileRepository.findAllByDocument_Id(documentId, pageable);

        if (files.isEmpty()) {
            throw new NoFilesPageFoundException(pageable.getPageNumber());
        }

        return files.map(FileResponseDto::mapFromEntity);
    }

    @Override
    public FileDownloadResponseDto downloadFile(Long documentId, Long fileId) throws IOException {

        if (!documentRepository.existsById(documentId)) {
            throw new DocumentDoesNotExistException(documentId);
        }

        File file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotExistException(fileId)
        );

        java.io.File downloadFile = new java.io.File(file.getPath());

        FileDownloadResponseDto responseDto = FileDownloadResponseDto.mapFromEntity(file);

        responseDto.setBytes(FileUtils.readFileToByteArray(downloadFile));

        return responseDto;
    }

    @Override
    public Page<FileResponseDto> updateFile(String username, MultipartFile file,
                                            Long documentId, Long fileId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        File updateFile = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotExistException(fileId)
        );

        try {
            FileUtils.writeByteArrayToFile(new java.io.File(
                    "src\\main\\resources\\fileBase\\"
                            + updateFile.getDocument().getCreator().getUsername() + "\\"
                            + "docId_" + updateFile.getDocument().getId() + "_"
                            + file.getOriginalFilename()), file.getBytes());
            multipartFileToFile(file, updateFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileRepository.save(updateFile);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        document.setUpdateDate(LocalDateTime.now());

        documentRepository.save(document);

        return new PageImpl<>(document.getFiles().stream().map(FileResponseDto::mapFromEntity).toList());
    }

    @Override
    public boolean deleteFile(String username, Long documentId, Long fileId) {

        if (!userRepository.existsByUsernameAndDocuments_id(username, documentId)) {
            throw new DocumentAccessException(username);
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotExistException(fileId));

        List<File> files = document.getFiles();

        Path filePath = Paths.get(file.getPath());

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from the filesystem");
        }

        files.remove(file);
        document.setFiles(files);
        document.setUpdateDate(LocalDateTime.now());
        documentRepository.save(document);

        return true;
    }


    private void multipartFileToFile(MultipartFile multipartFile, File file) throws IOException {
        file.setName(multipartFile.getOriginalFilename());
        file.setType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setPath("src\\main\\resources\\fileBase\\"
                + file.getDocument().getCreator().getUsername() + "\\"
                + "docId_" + file.getDocument().getId() + "_"
                + multipartFile.getOriginalFilename());
    }
}
