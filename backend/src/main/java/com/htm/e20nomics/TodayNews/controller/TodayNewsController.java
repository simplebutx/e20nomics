package com.htm.e20nomics.todaynews.controller;

import com.htm.e20nomics.todaynews.dto.TodayNewsDetailResponse;
import com.htm.e20nomics.todaynews.dto.TodayNewsResponse;
import com.htm.e20nomics.todaynews.service.TodayNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodayNewsController {

    private final TodayNewsService todayNewsService;

    // 사용자가 오늘의 뉴스 불러오기

    @GetMapping("/api/todayNews")
    public List<TodayNewsResponse> getTodayNews() {
        return todayNewsService.getPublicTodayNews();
    }

    @GetMapping("/api/todayNews/{id}")
    public TodayNewsDetailResponse getTodayNewsDetail(@PathVariable Long id) {
        return todayNewsService.getPublicTodayNewsDetail(id);
    }
}
