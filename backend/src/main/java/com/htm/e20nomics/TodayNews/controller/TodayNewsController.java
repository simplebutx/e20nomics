package com.htm.e20nomics.TodayNews.controller;

import com.htm.e20nomics.TodayNews.dto.PublicTodayNewsDetailResponse;
import com.htm.e20nomics.TodayNews.dto.PublicTodayNewsResponse;
import com.htm.e20nomics.TodayNews.service.TodayNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodayNewsController {

    private final TodayNewsService todayNewsService;

    @GetMapping("/api/todayNews")
    public List<PublicTodayNewsResponse> getTodayNews() {
        return todayNewsService.getPublicTodayNews();
    }

    @GetMapping("/api/todayNews/{id}")
    public PublicTodayNewsDetailResponse getTodayNewsDetail(@PathVariable Long id) {
        return todayNewsService.getPublicTodayNewsDetail(id);
    }
}
