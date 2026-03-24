package com.htm.e20nomics.todaynews.controller;


import com.htm.e20nomics.todaynews.dto.*;
import com.htm.e20nomics.todaynews.service.AdminTodayNewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminTodayNewsController {

    public final AdminTodayNewsService adminTodayNewsService;

    // 오늘의 뉴스

    @PostMapping("/api/admin/todayNews/generate")   // 요약 생성
    public AdminTodayNewsGenerateResponse createTodayNews(@RequestBody AdminTodayNewsGenerateRequest dto) {
        return adminTodayNewsService.summarize(dto.getText());
    }

    @PostMapping("/api/admin/todayNews")  // 요약 저장
    public ResponseEntity<Void> postTodayNews(@Valid @RequestBody AdminTodayNewsCreateRequest dto) {
        adminTodayNewsService.saveTodayNews(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/api/admin/todayNews")  // 뉴스 목록 가져오기
    public List<AdminTodayNewsResponse> getTodayNewsList() {
        return adminTodayNewsService.getTodayNewsList();
    }

    @GetMapping("/api/admin/todayNews/{id}")   // 상세
    public AdminTodayNewsDetailResponse getTodayNewsDetail(@PathVariable Long id) {
        return adminTodayNewsService.getTodayNewsDetail(id);
    }

    @PutMapping("/api/admin/todayNews/{id}")   // 수정
    public ResponseEntity<Void> updateTodayNews(@PathVariable Long id,
                                                @Valid @RequestBody AdminTodayNewsUpdateRequest dto) {
        adminTodayNewsService.updateTodayNews(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/admin/todayNews/{id}")   // 삭제
    public ResponseEntity<Void> deleteTodayNews(@PathVariable Long id) {
        adminTodayNewsService.deleteTodayNews(id);
        return ResponseEntity.noContent().build();
    }

    // 이미지 생성
    @PostMapping("/api/admin/todayNews/generateImage")
    public AdminImageGenerateResponse generateImage(@RequestBody AdminImageGenerateRequest dto) {
        return adminTodayNewsService.generateImage(dto.getPrompt());
    }

    // db에 이미지 url 저장
    @PutMapping("/api/admin/todayNews/{id}/image")
    public ResponseEntity<Void> postImageUrl(@PathVariable Long id, @RequestBody AdminTodayNewsImageUpdateRequest dto) {
        System.out.println("id = " + id);
        System.out.println("imageKey = " + dto.getImageKey());
        adminTodayNewsService.updateImage(id, dto.getImageKey());

        return ResponseEntity.ok().build();
    }
}
