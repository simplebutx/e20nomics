package com.htm.e20nomics.summary.controller;


import com.htm.e20nomics.summary.dto.SummaryRequest;
import com.htm.e20nomics.summary.dto.SummaryResponse;
import com.htm.e20nomics.summary.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/api/summaries")
    public SummaryResponse summarize(@Valid @RequestBody SummaryRequest request) {
        String summary = summaryService.summarize(request.text());
        return new SummaryResponse(summary);
    }
}


// controller -> service -> OpenAiChatClient(OpenAI API와 통신) -> service -> controller