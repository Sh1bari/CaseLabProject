package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileCreateResponseDto;

public interface FileService {

    FileCreateResponseDto addFile(FileCreateRequestDto request);
}
