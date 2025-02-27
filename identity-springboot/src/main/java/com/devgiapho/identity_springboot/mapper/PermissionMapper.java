package com.devgiapho.identity_springboot.mapper;

import com.devgiapho.identity_springboot.dto.request.PermissionRequest;
import com.devgiapho.identity_springboot.dto.respone.PermissionResponse;
import com.devgiapho.identity_springboot.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermisson(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
