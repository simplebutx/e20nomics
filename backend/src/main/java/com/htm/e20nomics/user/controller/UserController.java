package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/api/me")
    public MyPageResponse myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new MyPageResponse(userDetails.getUsername(), userDetails.getName());
    }
}
