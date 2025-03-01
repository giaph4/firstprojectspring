package com.devgiapho.identity_springboot.service;

import com.devgiapho.identity_springboot.dto.request.RoleRequest;
import com.devgiapho.identity_springboot.dto.respone.RoleResponse;
import com.devgiapho.identity_springboot.mapper.RoleMapper;
import com.devgiapho.identity_springboot.repository.PermissionRepository;
import com.devgiapho.identity_springboot.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class RoleService {
    RoleRepository repository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    final RoleRepository roleRepository;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
