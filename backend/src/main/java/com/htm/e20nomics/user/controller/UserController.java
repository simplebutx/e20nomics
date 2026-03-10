package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.user.dto.MyPageResponse;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import com.htm.e20nomics.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/api/me/terms")
    public List<MyTermResponse> myTerms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.myTerms(userDetails.getUserId());
    }
}
