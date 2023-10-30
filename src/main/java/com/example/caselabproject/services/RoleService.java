package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

public interface RoleService {
    @Transactional
    List<Role> findRolesByRoleDtoList(List<@Valid RoleDto> roleDtoList);
}
