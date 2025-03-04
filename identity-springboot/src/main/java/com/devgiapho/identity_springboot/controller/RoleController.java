package com.devgiapho.identity_springboot.controller;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import com.devgiapho.identity_springboot.dto.request.RoleRequest;
import com.devgiapho.identity_springboot.dto.respone.RoleResponse;
import com.devgiapho.identity_springboot.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class RoleController {

    RoleService roleService;

    @PostMapping()
    ApiRespone<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiRespone.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiRespone<List<RoleResponse>> getAll() {
        return ApiRespone.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiRespone<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return  ApiRespone.<Void>builder()
                .message("Delete succesfully")
                .build();
    }
}