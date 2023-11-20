package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.role.RoleDto;
import com.example.caselabproject.models.entities.Role;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {
    @Transactional
    List<Role> findRolesByRoleDtoList(List<@Valid RoleDto> roleDtoList);
}
