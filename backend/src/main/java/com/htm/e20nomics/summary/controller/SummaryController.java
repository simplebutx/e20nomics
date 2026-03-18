package com.htm.e20nomics.summary.controller;


import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.summary.dto.*;
import com.htm.e20nomics.summary.service.SummaryService;
import com.htm.e20nomics.summary.dto.SummariesResponse;
import com.htm.e20nomics.summary.dto.SummaryDetailResponse;
import com.htm.e20nomics.summary.dto.SummaryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/api/summaries/generate")
    public SummaryGenerateResponse summarize(@Valid @RequestBody SummaryGenerateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return summaryService.summarize(request.getText(), userDetails.getUserId());
    }
    // controller -> service -> OpenAiChatClient(OpenAI API와 통신) -> service -> controller

    @PostMapping("/api/summaries")
    public ResponseEntity<Void> saveSummary(@Valid @RequestBody SummaryCreateRequest dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        summaryService.saveSummary(dto, userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/api/me/summaries")
    public List<SummariesResponse> mySummaries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return summaryService.mySummaries(userDetails.getUserId());
    }

    @GetMapping("/api/me/summaries/{id}")
    public SummaryDetailResponse getSummaryDetail(@PathVariable Long id) {
        return summaryService.getSummaryDetail(id);
    }

    @PatchMapping("/api/me/summaries/{id}")
    public ResponseEntity<Void> updateSummary(
            @PathVariable Long id,
            @Valid @RequestBody SummaryUpdateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        summaryService.updateSummary(id, userDetails.getUserId(), dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/me/summaries/{id}")
    public ResponseEntity<Void> deleteSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        summaryService.deleteSummary(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}


