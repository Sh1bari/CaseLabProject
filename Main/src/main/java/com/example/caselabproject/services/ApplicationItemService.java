package com.example.caselabproject.services;


import com.example.caselabproject.models.DTOs.request.application.ApplicationItemVoteRequestDto;
import com.example.caselabproject.models.DTOs.request.application.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.request.application.RedirectApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.application.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Transactional
    RedirectApplicationItemResponseDto redirectApplicationItem(
            @Valid RedirectApplicationItemRequestDto redirectApplicationItemRequestDto,
            @Min(value = 1L, message = "Application id cant be less than 1.") Long applicationItemId,
            @NotBlank(message = "Application creator username cant be blank.") String username);

    @Transactional(readOnly = true)
    ApplicationItemGetByIdResponseDto getApplicationItemById(
            @Min(value = 1L, message = "Application id cant be less than 1.") Long applicationId,
            @Min(value = 1L, message = "Application item id cant be less than 1.") Long applicationItemId,
            @NotBlank(message = "Application creator username cant be blank.") String username);

    @Transactional
    ApplicationItemTakeResponseDto takeApplicationItem(
            @Min(value = 1L, message = "Application id cant be less than 1.") Long applicationId,
            @Min(value = 1L, message = "Application item id cant be less than 1.") Long applicationItemId,
            @NotBlank(message = "Application creator username cant be blank.") String username);

    @Transactional
    ApplicationItemVoteResponseDto voteApplicationItem(
            @Min(value = 1L, message = "Application id cant be less than 1.") Long applicationId,
            @Min(value = 1L, message = "Application item id cant be less than 1.") Long applicationItemId,
            @NotBlank(message = "Application creator username cant be blank.") String username,
            @NotNull(message = "Vote model cant be null") ApplicationItemVoteRequestDto voteApplicationItem);
}
