package com.devgiapho.identity_springboot.controller;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import com.devgiapho.identity_springboot.dto.request.PermissionRequest;
import com.devgiapho.identity_springboot.dto.respone.PermissionResponse;
import com.devgiapho.identity_springboot.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class PermissionController {
    PermissionService permissionService;

    @PostMapping()
    ApiRespone<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiRespone.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiRespone<List<PermissionResponse>> getAll() {
        return ApiRespone.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiRespone<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return  ApiRespone.<Void>builder()
                .message("Delete succesfully")
                .build();
    }
}
