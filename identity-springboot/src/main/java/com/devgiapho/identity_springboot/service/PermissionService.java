package com.devgiapho.identity_springboot.service;

import com.devgiapho.identity_springboot.dto.request.PermissionRequest;
import com.devgiapho.identity_springboot.dto.respone.PermissionResponse;
import com.devgiapho.identity_springboot.entity.Permission;
import com.devgiapho.identity_springboot.mapper.PermissionMapper;
import com.devgiapho.identity_springboot.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermisson(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    List<PermissionResponse> getAll() {
        var permissons = permissionRepository.findAll();
        return permissons.stream().map(permissionMapper::toPermissionResponse).toList();
    }
}
