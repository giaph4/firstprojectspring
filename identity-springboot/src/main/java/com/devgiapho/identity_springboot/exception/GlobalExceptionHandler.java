package com.devgiapho.identity_springboot.exception;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiRespone<Void>> handlingRuntimeException(RuntimeException e) {
        ApiRespone<Void> apiRespone = new ApiRespone<>();

        apiRespone.setCode(ErrorCode.UNCATED.getCode());
        apiRespone.setMessage(ErrorCode.UNCATED.getMessage());

        return ResponseEntity
                .badRequest().body(apiRespone);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespone<Void>> handlingAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        ApiRespone<Void> apiRespone = new ApiRespone<>();

        apiRespone.setCode(errorCode.getCode());
        apiRespone.setMessage(e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiRespone);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiRespone<?>> handlingAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiRespone.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf((enumKey));
        ApiRespone<Void> apiRespone = new ApiRespone<>();

        apiRespone.setCode(errorCode.getCode());
        apiRespone.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }
}