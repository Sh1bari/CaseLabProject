package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.RoleNameNotFoundException;
import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.entities.Role;
import com.example.caselabproject.repositories.RoleRepository;
import com.example.caselabproject.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;

    @Override
    public List<Role> findRolesByRoleDtoList(List<RoleDto> roleDtoList) {
        List<Role> roleList = new ArrayList<>();
        roleDtoList.forEach(o -> {
            roleList.add(roleRepo.findByName(o.getName())
                    .orElseThrow(() -> new RoleNameNotFoundException(o.getName())));
        });
        return roleList;
    }
}
