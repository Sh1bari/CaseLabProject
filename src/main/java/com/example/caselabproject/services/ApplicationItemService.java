package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.CreateApplicationItemResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Description: Service for Application Item entity
 *
 * @author Vladimir Krasnov
 */
@Validated
public interface ApplicationItemService {
    @Transactional
    List<CreateApplicationItemResponseDto> createApplicationItem(
            List<@Valid CreateApplicationItemRequestDto> createApplicationItemList,
            @Min(value = 1L, message = "Application id cant be less than 1.") Long applicationId,
            @NotBlank(message = "Application creator username cant be blank.") String username);
}
