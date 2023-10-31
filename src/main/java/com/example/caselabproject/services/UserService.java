package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
public interface UserService {

    @Transactional(readOnly = true)
    UserGetByIdResponseDto getById(@Min(1L) Long id);

    @Transactional
    UserCreateResponseDto create(@Valid UserCreateRequestDto userRequestDto);

    @Transactional
    UserUpdateResponseDto updateById(Long id, UserUpdateRequestDto userUpdateRequestDto);

    @Transactional
    UserDeleteResponseDto deleteById(Long id);

    @Transactional
    List<DocumentCreateResponseDto> findDocsByCreatorId(Long id);
}
