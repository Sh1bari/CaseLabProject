package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    @Transactional
    boolean existById(Long id);

    @Transactional
    UserCreateResponseDto create(@Valid UserCreateRequestDto userRequestDto);

    @Transactional
    UserUpdateResponseDto updateById(@Min(value = 1L, message = "Id can't be less than 1") Long id, @Valid UserUpdateRequestDto userUpdateRequestDto);

    @Transactional
    UserDeleteResponseDto deleteById(@Min(value = 1L, message = "Id can't be less than 1") Long id);

    @Transactional
    UserRecoverResponseDto recoverById(@Min(value = 1L, message = "Id can't be less than 1") Long id);

    @Transactional
    List<DocumentCreateResponseDto> findDocsByFiltersByPage(@Min(value = 1L, message = "Id can't be less than 1")
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
    List<ApplicationFindResponseDto> findApplicationsByCreatorIdByPage(@Min(value = 1L, message = "Id can't be less than1") Long id,
                                                                       Pageable pageable);
}

