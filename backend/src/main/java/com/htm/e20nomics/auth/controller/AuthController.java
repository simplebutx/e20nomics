package com.htm.e20nomics.auth.controller;


import com.htm.e20nomics.auth.dto.LoginRequest;
import com.htm.e20nomics.auth.dto.SignupRequest;
import com.htm.e20nomics.auth.dto.TokenResponse;
import com.htm.e20nomics.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest dto) {
        authService.signup(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/api/auth/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest dto) {
        return authService.login(dto);
    }
}
