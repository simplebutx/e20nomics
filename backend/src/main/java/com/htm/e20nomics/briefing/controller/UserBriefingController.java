package com.htm.e20nomics.briefing.controller;

import com.htm.e20nomics.briefing.domain.Briefing;
import com.htm.e20nomics.briefing.dto.UserBriefingCreateRequest;
import com.htm.e20nomics.briefing.dto.UserBriefingResponse;
import com.htm.e20nomics.briefing.service.BriefingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/briefings")
public class UserBriefingController {

    private final BriefingService briefingService;

    @PostMapping
    public UserBriefingResponse create(@Valid @RequestBody UserBriefingCreateRequest request) {
        Briefing saved = briefingService.createUserBriefing(request.getText());
        return new UserBriefingResponse(saved.getId(), saved.getSummaryText());
    }
}