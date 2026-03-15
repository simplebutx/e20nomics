package com.htm.e20nomics.user.controller;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import com.htm.e20nomics.user.dto.MySummaryDetailResponse;
import com.htm.e20nomics.user.dto.MySummaryUpdateRequest;
import com.htm.e20nomics.user.service.MySummaryService;
import com.htm.e20nomics.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MySummaryController {

    public final MySummaryService mySummaryService;

    @GetMapping("/api/me/summaries")
    public List<MySummariesResponse> mySummaries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return mySummaryService.mySummaries(userDetails.getUserId());
    }

    @GetMapping("/api/me/summaries/{id}")
    public MySummaryDetailResponse getSummaryDetail(@PathVariable Long id) {
        return mySummaryService.getSummaryDetail(id);
    }

    @PatchMapping("/api/me/summaries/{id}")
    public ResponseEntity<Void> updateSummary(
            @PathVariable Long id,
            @RequestBody MySummaryUpdateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        mySummaryService.updateSummary(id, userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/me/summaries/{id}")
    public ResponseEntity<Void> deleteSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        mySummaryService.deleteSummary(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
