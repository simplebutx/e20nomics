package com.htm.e20nomics.summary.controller;


import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.summary.dto.SummaryCreateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.summary.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/api/summaries/generate")
    public SummaryGenerateResponse summarize(@Valid @RequestBody SummaryGenerateRequest request) {
        return summaryService.summarize(request.text());
    }
    // controller -> service -> OpenAiChatClient(OpenAI API와 통신) -> service -> controller

    @PostMapping("/api/summaries")
    public ResponseEntity<Void> saveSummary(@Valid @RequestBody SummaryCreateRequest dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        summaryService.saveSummary(dto.getOriginalText(), dto.getSummaryText(), dto.isPublic(), userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }
}


