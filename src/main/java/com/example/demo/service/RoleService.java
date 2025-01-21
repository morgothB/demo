package com.example.demo.service;

import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.RoleResponse;
import com.example.demo.entity.Role;
import com.example.demo.exception.DataIntegrityException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleRepository.save(mapper.map(request));
        return mapper.map(role);
    }

    public RoleResponse updateRole(UUID roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new DataIntegrityException("Role does not exist"));
        role.setName(request.getName());
        role = roleRepository.save(role);
        return mapper.map(role);
    }

    public void deleteRole(UUID roleId) {
        roleRepository.deleteById(roleId);
    }

    public List<RoleResponse> getRolesList() {
        List<Role> roles = roleRepository.findAll();
        return mapper.map(roles);
    }

}
