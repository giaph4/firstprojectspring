package com.devgiapho.identity_springboot.mapper;

import com.devgiapho.identity_springboot.dto.request.UserCreationRequest;
import com.devgiapho.identity_springboot.dto.request.UserUpdateRequest;
import com.devgiapho.identity_springboot.dto.respone.UserResponse;
import com.devgiapho.identity_springboot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
