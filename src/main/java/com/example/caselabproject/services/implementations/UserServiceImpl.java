package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.DocumentPageRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DocumentPageRepository documentPageRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepo;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
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
        if (userUpdateRequestDto.getPosition() != null) {
            user.setPosition(userUpdateRequestDto.getPosition());
        }
        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getDepartmentId() != null) {
            user.setDepartment(departmentRepo.findById(userUpdateRequestDto.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(userUpdateRequestDto.getDepartmentId())));
        }
        if (userUpdateRequestDto.getRoles() != null) {
            user.setRoles(roleService.findRolesByRoleDtoList(userUpdateRequestDto.getRoles()));
        }
        if (userUpdateRequestDto.getPassword() != null) {
            user.getAuthUserInfo().setPassword(userUpdateRequestDto.getPassword());
        }

        if (userUpdateRequestDto.getEmail() != null) {
            user.getAuthUserInfo().setEmail(userUpdateRequestDto.getEmail());
        }

        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }

        if (userUpdateRequestDto.getFirstName() != null) {
            user.getPersonalUserInfo().setFirstName(userUpdateRequestDto.getFirstName());
        }

        if (userUpdateRequestDto.getPatronymic() != null) {
            user.getPersonalUserInfo().setPatronymic(userUpdateRequestDto.getPatronymic());
        }

        if (userUpdateRequestDto.getLastName() != null) {
            user.getPersonalUserInfo().setLastName(userUpdateRequestDto.getLastName());
        }

        if (userUpdateRequestDto.getBirthDate() != null) {
            user.getPersonalUserInfo().setBirthDate(userUpdateRequestDto.getBirthDate());
        }


        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(userUpdateRequestDto.getUsername());
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
    public UserRecoverResponseDto recoverById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRecordState(RecordState.ACTIVE);
        userRepository.save(user);
        return UserRecoverResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public List<DocumentCreateResponseDto> findDocsByCreatorIdByPage(Long id,
                                                                     String name,
                                                                     Pageable pageable) {
        if (existById(id)) {
            List<DocumentCreateResponseDto> documentCreateResponseDtoList = DocumentCreateResponseDto
                    .mapFromListOfEntities(documentPageRepository.findAllByCreator_idAndNameContainingIgnoreCase(id, name, pageable).toList());
            return documentCreateResponseDtoList;
        } else throw new UserNotFoundException(id);
    }
}
