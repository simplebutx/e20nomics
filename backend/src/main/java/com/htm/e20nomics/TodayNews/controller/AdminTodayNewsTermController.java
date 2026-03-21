package com.htm.e20nomics.todaynews.controller;

import com.htm.e20nomics.todaynews.dto.TodayNewsTermRequest;
import com.htm.e20nomics.todaynews.service.AdminTodayNewsService;
import com.htm.e20nomics.todaynews.service.AdminTodayNewsTermService;
import com.htm.e20nomics.todaynews.service.TodayNewsService;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminTodayNewsTermController {

    public final AdminTodayNewsTermService adminTodayNewsTermService;

    // 오늘의 뉴스 단어

    // 현재 오늘의 뉴스와 연결되어있는 단어목록 조회
    @GetMapping("/api/admin/todayNews/{todayNewsId}/terms")
    public ResponseEntity<List<AdminTermResponse>> getLinkedTerms(@PathVariable Long todayNewsId) {
        return ResponseEntity.ok(adminTodayNewsTermService.getLinkedTerms(todayNewsId));
    }

    // 현재 오늘의 뉴스와 term 연결시키기
    @PostMapping("/api/admin/todayNews/{todayNewsId}/terms")
    public ResponseEntity<Void> linkTerm(@PathVariable Long todayNewsId,
                                         @RequestBody TodayNewsTermRequest request) {
        adminTodayNewsTermService.linkTerm(todayNewsId, request.getTermId());
        return ResponseEntity.ok().build();
    }

    // 현재 오늘의 뉴스와 term 연결 해제시키기
    @DeleteMapping("/api/admin/todayNews/{todayNewsId}/terms/{termId}")
    public ResponseEntity<Void> unlinkTerm(@PathVariable Long todayNewsId,
                                           @PathVariable Long termId) {
        adminTodayNewsTermService.unlinkTerm(todayNewsId, termId);
        return ResponseEntity.noContent().build();
    }
}
