package com.devgiapho.identity_springboot.controller;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import com.devgiapho.identity_springboot.dto.request.AuthenticationRequest;
import com.devgiapho.identity_springboot.dto.request.IntrospectRequest;
import com.devgiapho.identity_springboot.dto.respone.AuthenticationResponse;
import com.devgiapho.identity_springboot.dto.respone.IntrospectResponse;
import com.devgiapho.identity_springboot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiRespone<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        return ApiRespone.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiRespone<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiRespone.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
