package com.devgiapho.identity_springboot.service;

import com.devgiapho.identity_springboot.constant.PredefinedRole;
import com.devgiapho.identity_springboot.dto.request.UserCreationRequest;
import com.devgiapho.identity_springboot.dto.request.UserUpdateRequest;
import com.devgiapho.identity_springboot.dto.respone.UserResponse;
import com.devgiapho.identity_springboot.entity.Role;
import com.devgiapho.identity_springboot.entity.User;
import com.devgiapho.identity_springboot.exception.AppException;
import com.devgiapho.identity_springboot.exception.ErrorCode;
import com.devgiapho.identity_springboot.mapper.UserMapper;
import com.devgiapho.identity_springboot.repository.RoleRepository;
import com.devgiapho.identity_springboot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service // Đánh dấu class này là một Spring Service
@RequiredArgsConstructor // Tự động tạo constructor cho các field final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Đặt tất cả field là private và final
@Slf4j // Tạo logger tự động bằng Lombok
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }


    //    @PreAuthorize("hasRole('ADMIN')") // Kiểm tra quyền trước khi thực thi
    @PreAuthorize("hasAuthority('SEE_LIST_USER')")
    public List<UserResponse> getUsers() {
        log.info("Get all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name") // Chỉ cho phép user xem thông tin của chính họ
    public UserResponse getUser(String userId) {
        log.info("Get user by id: {}", userId);
        return userMapper.toUserResponse(
                userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public User updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}