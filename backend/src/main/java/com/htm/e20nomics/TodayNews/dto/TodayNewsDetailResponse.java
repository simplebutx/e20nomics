package com.htm.e20nomics.todaynews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodayNewsDetailResponse {
    private String summaryTitle;
    private String summaryText;
    private LocalDateTime createdAt;
    private List<TodayNewsTermResponse> terms;
}
