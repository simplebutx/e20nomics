package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.user.dto.*;
import com.htm.e20nomics.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/api/me/terms")
    public List<MyTermResponse> myTerms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.myTerms(userDetails.getUserId());
    }

    @GetMapping("/api/me/terms/{id}")
    public MyTermDetailResponse getTermDetail(@PathVariable Long id) {
        return userService.getTermDetail(id);
    }

    @PutMapping("/api/me/terms/{id}")
    public ResponseEntity<Void> updateMyTerm(@PathVariable Long id,
                                             @Valid @RequestBody MyTermUpdateRequest dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.updateMyTerm(id, dto, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/me/terms/{id}")
    public ResponseEntity<Void> deleteMyTerm(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteMyTerm(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
