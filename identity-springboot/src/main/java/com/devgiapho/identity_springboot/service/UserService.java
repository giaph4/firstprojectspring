package com.devgiapho.identity_springboot.service;

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
    // Các dependency được inject qua constructor
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    // Tạo mới user từ request
    public User createUser(UserCreationRequest request) {
        // Kiểm tra xem username đã tồn tại chưa
        log.info("Role: {}", roleRepository.findById("USER"));
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        // Chuyển đổi request thành entity User
        User user = userMapper.toUser(request);
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Gán role mặc định là "USER"

        Role role = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(new HashSet<>(List.of(role)));
        // Lưu user vào database và trả về
        return userRepository.save(user);
    }

    // Lấy danh sách tất cả user - chỉ admin mới truy cập được
    @PreAuthorize("hasRole('ADMIN')") // Kiểm tra quyền trước khi thực thi
    public List<UserResponse> getUsers() {
        log.info("Get all users");
        // Lấy tất cả user từ DB và chuyển thành list UserResponse
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    // Lấy thông tin user theo ID - kiểm tra quyền sau khi thực thi
    @PostAuthorize("returnObject.username == authentication.name") // Chỉ cho phép user xem thông tin của chính họ
    public UserResponse getUser(String userId) {
        log.info("Get user by id: {}", userId);
        // Tìm user theo ID, nếu không tìm thấy thì throw exception
        return userMapper.toUserResponse(
                userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTS))
        );
    }

    // Lấy thông tin của user hiện đang đăng nhập
    public UserResponse getMyInfo() {
        // Lấy context bảo mật hiện tại
        var context = SecurityContextHolder.getContext();
        // Lấy username từ authentication
        String name = context.getAuthentication().getName();

        // Tìm user theo username, nếu không tìm thấy thì throw exception
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTS));
        // Chuyển thành UserResponse và trả về
        return userMapper.toUserResponse(user);
    }

    // Cập nhật thông tin user
    @PostAuthorize("returnObject.username == authentication.name")
    public User updateUser(String userId, UserUpdateRequest request) {
        // Tìm user theo ID, nếu không tìm thấy thì throw exception
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTS));
        // Cập nhật thông tin user từ request
        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        // Lưu thay đổi vào database và trả về
        return userRepository.save(user);
    }

    // Xóa user theo ID
    public void deleteUser(String userId) {
        // Xóa user khỏi database dựa trên ID
        userRepository.deleteById(userId);
    }
}