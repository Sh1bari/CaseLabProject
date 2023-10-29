package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationDeleteRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationDeleteResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;

public interface ApplicationService {
    ApplicationCreateResponseDto createApplication(ApplicationCreateRequestDto request);
    ApplicationUpdateResponseDto updateApplication(Long id, ApplicationUpdateRequestDto request);
    ApplicationDeleteResponseDto deleteApplication(ApplicationDeleteRequestDto request);
    ApplicationFindResponseDto getApplicationById(Long id);
}
