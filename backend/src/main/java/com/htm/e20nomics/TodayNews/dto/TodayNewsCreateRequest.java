package com.htm.e20nomics.TodayNews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayNewsCreateRequest {
    private String originalText;
    private String summaryTitle;
    private String summaryText;
}
