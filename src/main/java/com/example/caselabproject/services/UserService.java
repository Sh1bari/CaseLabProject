package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.user.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.user.UserUpdatePasswordRequest;
import com.example.caselabproject.models.DTOs.request.user.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentGetAllResponse;
import com.example.caselabproject.models.DTOs.response.application.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.user.*;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface UserService {

    @Transactional(readOnly = true)
    UserGetByIdResponseDto getById(@Min(value = 1L, message = "Id can't be less than 1") Long id);

    @Transactional(readOnly = true)
    UserGetByIdResponseDto getByUsername(@NotBlank(message = "Username cant be null") String username);


    @Transactional
    boolean existById(Long id);

    @Transactional
    UserCreateResponseDto create(@Valid UserCreateRequestDto userRequestDto);

    @Transactional
    UserUpdateResponseDto updateById(@Min(value = 1L, message = "Id can't be less than 1") Long id,
                                     @Valid UserUpdateRequestDto userUpdateRequestDto);

    @Transactional
    UserUpdateResponseDto updatePasswordById(@Min(value = 1L, message = "Id can't be less than 1") Long id,
                                             @Valid UserUpdatePasswordRequest req);

    @Transactional
    UserDeleteResponseDto deleteById(@Min(value = 1L, message = "Id can't be less than 1") Long id);

    @Transactional
    UserRecoverResponseDto recoverById(@Min(value = 1L, message = "Id can't be less than 1") Long id);

    @Transactional
    UserUpdateResponseDto appointDirector(
            @Min(value = 1L, message = "Id can't be less than 1.") Long departmentId,
            @Min(value = 1L, message = "Id can't be less than 1.") Long userId);

    @Transactional
    List<DocumentGetAllResponse> findDocsByFiltersByPage(@Min(value = 1L, message = "Id can't be less than 1")
                                                         Long creatorId,
                                                         String name,
                                                         LocalDateTime creationDateFrom,
                                                         LocalDateTime creationDateTo,
                                                         Long documentConstructorTypeId,
                                                         RecordState recordState,
                                                         Pageable pageable);

    @Transactional
    List<UserGetByIdResponseDto> findAllUsersByFiltersByPage(String roleName,
                                                             String departmentName,
                                                             String firstName,
                                                             String lastName,
                                                             String patronymic,
                                                             LocalDate birthDateFrom,
                                                             LocalDate birthDateTo,
                                                             String email,
                                                             Pageable pageable
    );

    @Transactional
    List<ApplicationFindResponseDto> findApplicationsByCreatorIdByPage(
            @Min(value = 1L, message = "Id can't be less than 1.") Long id,
            RecordState recordState,
            Pageable pageable);

    @Transactional
    List<ApplicationItemGetByIdResponseDto> findApplicationItemsByUserIdByPage(
            @Min(value = 1L, message = "Id can't be less than 1.") Long id,
            String applicationName,
            ApplicationItemStatus status,
            RecordState recordState,
            Pageable pageable,
            String username);
}

