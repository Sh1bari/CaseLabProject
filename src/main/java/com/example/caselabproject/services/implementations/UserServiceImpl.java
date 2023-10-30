package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    @Override
    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto) {
        User user = userRequestDto.mapToEntity();
        user.setRoles(roleService.findRolesByRoleDtoList(userRequestDto.getRoles()));
        user.getAuthUserInfo().setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(user.getUsername());
        }
        return UserCreateResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserUpdateResponseDto updateById(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setPosition(userUpdateRequestDto.getPosition());
        user.setUsername(userUpdateRequestDto.getUsername());
        user.setDepartment(userUpdateRequestDto.getDepartment());
        user.setRecordState(userUpdateRequestDto.getRecordState());
        user.setRoles(userUpdateRequestDto.mapToEntity().getRoles());
        user.setAuthUserInfo(userUpdateRequestDto.mapToEntity().getAuthUserInfo());
        user.setPersonalUserInfo(userUpdateRequestDto.mapToEntity().getPersonalUserInfo());
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(user.getUsername());
        }
        return UserUpdateResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserDeleteResponseDto deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRecordState(RecordState.DELETED);
        userRepository.save(user);
        return UserDeleteResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public List<DocumentCreateResponseDto> findDocsByCreatorId(Long id) {
        List<DocumentCreateResponseDto> documentCreateResponseDtos = documentRepository.findAllByCreator_id(id);
        return documentCreateResponseDtos;
    }
}
