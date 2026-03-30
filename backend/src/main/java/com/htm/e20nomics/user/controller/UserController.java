package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.dto.*;
import com.htm.e20nomics.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/me")
    public MyPageResponse myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new MyPageResponse(userDetails.getUsername(), userDetails.getId());
    }

    @PutMapping("/api/me/preferences")
    public ResponseEntity<Void> savePreference(@Valid @RequestBody MyPreferenceCreateRequest dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.saveOrUpdate(dto, userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }
}
