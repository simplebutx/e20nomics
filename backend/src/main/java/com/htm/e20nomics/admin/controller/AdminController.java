package com.htm.e20nomics.admin.controller;

import com.htm.e20nomics.TodayNews.dto.*;
import com.htm.e20nomics.TodayNews.service.TodayNewsService;
import com.htm.e20nomics.admin.dto.AdminUserListResponse;
import com.htm.e20nomics.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;
    public final TodayNewsService todayNewsService;

    @GetMapping("/api/admin/users")
    public List<AdminUserListResponse> getUserList() {
        return adminService.getUserList();
    }

    @PostMapping("/api/admin/todayNews/generate")   // 요약 생성
    public TodayNewsGenerateResponse createTodayNews(@RequestBody TodayNewsGenerateRequest dto) {
        return todayNewsService.summarize(dto.getText());
    }

    @PostMapping("/api/admin/todayNews")  // 요약 저장
    public ResponseEntity<Void> postTodayNews(@Valid @RequestBody TodayNewsCreateRequest dto) {
        todayNewsService.saveTodayNews(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/api/admin/todayNews")
    public List<TodayNewsResponse> getTodayNewsList() {
        return todayNewsService.getTodayNewsList();
    }

    @GetMapping("/api/admin/todayNews/{id}")   // 상세
    public TodayNewsDetailResponse getTodayNewsDetail(@PathVariable Long id) {
        return todayNewsService.getTodayNewsDetail(id);
    }

    @PutMapping("/api/admin/todayNews/{id}")   // 수정
    public ResponseEntity<Void> updateTodayNews(@PathVariable Long id,
                                                @Valid @RequestBody TodayNewsUpdateRequest dto) {
        todayNewsService.updateTodayNews(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/admin/todayNews/{id}")   // 삭제
    public ResponseEntity<Void> deleteTodayNews(@PathVariable Long id) {
        todayNewsService.deleteTodayNews(id);
        return ResponseEntity.noContent().build();
    }
}
