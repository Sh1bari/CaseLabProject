package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.FileUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;

import java.util.List;

public interface FileService {

    List<FileResponseDto> addFile(String username, FileCreateRequestDto request, Long documentId);

    List<FileResponseDto> getFiles(Long documentId, Integer page);

    List<FileResponseDto> updateFile(String username, FileUpdateRequestDto request, Long documentId, Long fileId);

    List<FileResponseDto> deleteFile(String username, Long documentId, Long fileId);
}
