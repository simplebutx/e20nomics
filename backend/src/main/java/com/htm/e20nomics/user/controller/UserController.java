package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.dto.*;
import com.htm.e20nomics.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping("/api/me")
    public MyPageResponse myPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new MyPageResponse(userDetails.getUsername(), userDetails.getName(), userDetails.getDisplayName());
    }

    @GetMapping("/api/me/summaries")
    public List<MySummariesResponse> mySummaries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.mySummaries(userDetails.getUserId());
    }

    @GetMapping("/api/me/summaries/{id}")
    public MySummaryDetailResponse getSummaryDetail(@PathVariable Long id) {
        return userService.getSummaryDetail(id);
    }
}
