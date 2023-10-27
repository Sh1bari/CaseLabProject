package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileCreateResponseDto;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.repositories.FileRepository;
import com.example.caselabproject.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public FileCreateResponseDto addFile(FileCreateRequestDto request) {

        File file = request.mapToEntity();
        try {
            fileRepository.save(file);
        } catch (Exception e) {
            throw new DocumentDoesNotExistException(228L);
        }

        return FileCreateResponseDto.mapFromEntity(file);
    }
}
