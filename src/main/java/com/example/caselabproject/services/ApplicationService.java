package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.application.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.application.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationUpdateResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ApplicationService {
    ApplicationCreateResponseDto createApplication(@NotBlank String username, @Valid ApplicationCreateRequestDto request);

    ApplicationUpdateResponseDto updateApplication(@Min(value = 1L, message = "Id cant be less than 1") Long id, @NotBlank String username, @Valid ApplicationUpdateRequestDto request);

    boolean deleteApplication(@Min(value = 1L, message = "Id cant be less than 1") Long id, @NotBlank String username);

    ApplicationFindResponseDto getApplicationById(@Min(value = 1L, message = "Id cant be less than 1") Long id);

}
