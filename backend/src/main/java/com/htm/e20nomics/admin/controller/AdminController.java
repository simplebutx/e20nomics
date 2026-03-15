package com.htm.e20nomics.admin.controller;

import com.htm.e20nomics.admin.dto.AdminCreateTermRequest;
import com.htm.e20nomics.admin.dto.AdminTermResponse;
import com.htm.e20nomics.admin.dto.AdminUserListResponse;
import com.htm.e20nomics.admin.service.AdminService;
import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.summary.dto.SummaryCreateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.summary.service.SummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;
    public final SummaryService summaryService;

    @GetMapping("/api/admin/users")
    public List<AdminUserListResponse> getUserList() {
        return adminService.getUserList();
    }

    @PostMapping("/api/admin/announcements/generate")   // 요약 생성
    public SummaryGenerateResponse createAnnouncement(@RequestBody SummaryGenerateRequest request) {
        return summaryService.summarize(request.getText());
    }

    @PostMapping("/api/admin/announcements")  // 요약 저장
    public ResponseEntity<Void> postAnnouncement(@Valid @RequestBody SummaryCreateRequest dto,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        summaryService.postAnnouncement(dto, userDetails.getUserId());
        return ResponseEntity.status(201).build();
    }
}
