package com.devgiapho.identity_springboot.dto.request;

import com.devgiapho.identity_springboot.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    @DobConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    List<String> roles;
}
