package com.htm.e20nomics.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank
    String email;

    @NotBlank
    String password;
}
