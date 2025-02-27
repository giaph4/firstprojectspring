package com.devgiapho.identity_springboot.service;

import com.devgiapho.identity_springboot.dto.request.RoleRequest;
import com.devgiapho.identity_springboot.dto.respone.RoleResponse;
import com.devgiapho.identity_springboot.mapper.RoleMapper;
import com.devgiapho.identity_springboot.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class RoleService {
    RoleRepository repository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {

        return null;
    }
}
