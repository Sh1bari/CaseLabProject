package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.FileUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface FileService {

    List<FileResponseDto> addFile(String username,
                                  @Valid FileCreateRequestDto request,
                                  @Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    List<FileResponseDto> getFiles(@Min(value = 1L, message = "Id can't be less than 1") Long documentId, Pageable pageable);

    List<FileResponseDto> updateFile(String username,
                                     @Valid FileUpdateRequestDto request,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long documentId,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long fileId);

    List<FileResponseDto> deleteFile(String username,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long documentId,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long fileId);
}
