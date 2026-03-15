package com.htm.e20nomics.summary.controller;


import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.summary.dto.*;
import com.htm.e20nomics.summary.service.SummaryService;
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
    public SummaryGenerateResponse summarize(@Valid @RequestBody SummaryGenerateRequest request) {
        return summaryService.summarize(request.getText());
    }
    // controller -> service -> OpenAiChatClient(OpenAI API와 통신) -> service -> controller

    @PostMapping("/api/summaries")
    public ResponseEntity<Void> saveSummary(@Valid @RequestBody SummaryCreateRequest dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        summaryService.saveSummary(dto, userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/api/announcements")
    public List<AnnouncementsResponse> getAnnouncements() {
        return summaryService.getAnnouncements();
    }

    @GetMapping("/api/announcements/{id}")
    public AnnouncementDetailResponse getAnnouncementDetail(@PathVariable Long id) {
        return summaryService.getAnnouncementDetail(id);
    }
}


