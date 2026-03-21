package com.htm.e20nomics.todaynews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminTodayNewsDetailResponse {

    private String summaryTitle;
    private String summaryText;
    private Boolean isPublished;
    private LocalDateTime createdAt;
}
