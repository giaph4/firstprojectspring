package com.devgiapho.identity_springboot.controller;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import com.devgiapho.identity_springboot.dto.request.UserCreationRequest;
import com.devgiapho.identity_springboot.dto.request.UserUpdateRequest;
import com.devgiapho.identity_springboot.dto.respone.UserResponse;
import com.devgiapho.identity_springboot.entity.User;
import com.devgiapho.identity_springboot.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    ApiRespone<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiRespone.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiRespone<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("UserName: {}", authentication.getName());
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            log.info(grantedAuthority.getAuthority());
        }

        return ApiRespone.<List<UserResponse>>builder()
                .result(userService.getUsers()) // Trả về List<UserRespone>
                .build();
    }


    @GetMapping("/{userId}")
    ApiRespone<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiRespone.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiRespone<UserResponse> getMyInfo() {
        return ApiRespone.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    ApiRespone<User> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        ApiRespone<User> userApiRespone = new ApiRespone<>();
        userApiRespone.setMessage("User updated successfully: " + userId);
        userApiRespone.setResult(userService.updateUser(userId, request));
        return userApiRespone;
    }

    @DeleteMapping("/{userId}")
    ApiRespone<String> deleteUser(@PathVariable String userId) {
        ApiRespone<String> userApiRespone = new ApiRespone<>();
        userApiRespone.setMessage("User deleted successfully: " + userId);
        userApiRespone.setResult("User deleted successfully");
        userService.deleteUser(userId);
        return userApiRespone;
    }
}
