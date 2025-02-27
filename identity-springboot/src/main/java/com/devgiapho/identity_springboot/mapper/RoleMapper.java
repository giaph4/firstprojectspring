package com.devgiapho.identity_springboot.mapper;

import com.devgiapho.identity_springboot.dto.request.RoleRequest;
import com.devgiapho.identity_springboot.dto.respone.RoleResponse;
import com.devgiapho.identity_springboot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toPermisson(RoleRequest request);

    RoleResponse toRoleResponse(Role permission);
}
