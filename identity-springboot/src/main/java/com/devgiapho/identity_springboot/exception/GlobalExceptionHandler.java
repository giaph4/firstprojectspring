package com.devgiapho.identity_springboot.exception;

import com.devgiapho.identity_springboot.dto.request.ApiRespone;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    // thêm xử lý cho RuntimeException
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiRespone<Void>> handlingRuntimeException() {
        ApiRespone<Void> apiRespone = new ApiRespone<>();

        apiRespone.setCode(ErrorCode.UNCATED.getCode());
        apiRespone.setMessage(ErrorCode.UNCATED.getMessage());

        return ResponseEntity
                .badRequest().body(apiRespone);
    }


    // thêm xử lý cho AppException
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

    // thêm xử lý cho AccessDeniedException
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiRespone<?>> handlingAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiRespone.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // thêm xử lý cho MethodArgumentNotValidException
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf((enumKey));

        var constrainViolation = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

        Map<String, Object> attributes = constrainViolation.getConstraintDescriptor().getAttributes();

        log.info(attributes.toString());

        ApiRespone<Void> apiRespone = new ApiRespone<>();

        apiRespone.setCode(errorCode.getCode());
        apiRespone.setMessage(mapAttribute(errorCode.getMessage(), attributes));
        return ResponseEntity.badRequest().body(apiRespone);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}